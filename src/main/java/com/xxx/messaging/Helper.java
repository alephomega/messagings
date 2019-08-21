package com.xxx.messaging;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
class Helper {
    private final AmazonS3 s3;

    @Autowired
    Helper(AmazonS3 s3) {
        this.s3 = s3;
    }

    Recipients recipients(Messaging messaging) {
        String to = messaging.getTo();

        AmazonS3URI s3URI = null;
        try {
            s3URI = new AmazonS3URI(to);
        } catch (Exception ignore) { }

        if (s3URI != null) {
            final String bucket = s3URI.getBucket();
            final String key = s3URI.getKey();

            return new Recipients() {

                @Override
                public Iterator<String> iterator() {
                    return new Iterator<String>() {
                        private static final String OBJECT_KEY_DELIMITER = "/";

                        private int i = 0;
                        private List<String> keys;
                        private LineIterator lineIterator;

                        private Iterator<String> init() {
                            ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                                    .withBucketName(bucket)
                                    .withPrefix(key)
                                    .withMarker(key)
                                    .withDelimiter(OBJECT_KEY_DELIMITER);
                            this.keys = new ArrayList<>();

                            ObjectListing objectListing = s3.listObjects(listObjectsRequest);
                            List<S3ObjectSummary> summaries = objectListing.getObjectSummaries();
                            for (S3ObjectSummary summary : summaries) {
                                this.keys.add(summary.getKey());
                            }

                            return this;
                        }

                        @Override
                        public boolean hasNext() {
                            while (lineIterator == null || !lineIterator.hasNext()) {
                                if (i >= keys.size()) {
                                    return false;
                                }

                                try {
                                    lineIterator = IOUtils.lineIterator(s3.getObject(bucket, keys.get(i++)).getObjectContent(), StandardCharsets.UTF_8.name());
                                } catch (IOException e) {
                                    lineIterator = null;
                                }
                            }

                            return true;
                        }

                        @Override
                        public String next() {
                            if (hasNext()) {
                                return lineIterator.next();
                            }

                            throw new NoSuchElementException();
                        }
                    }.init();
                }
            };
        } else if (to.contains(",")) {
            return new Recipients() {
                @Override
                public Iterator<String> iterator() {
                    return Arrays.stream(to.split(",")).filter(s -> !s.trim().isEmpty()).iterator();
                }
            };
        } else {
            return new Recipients() {
                @Override
                public Iterator<String> iterator() {
                    return new Iterator<String>() {
                        private boolean hasNext = true;

                        @Override
                        public boolean hasNext() {
                            return hasNext;
                        }

                        @Override
                        public String next() {
                            hasNext = false;
                            return to;
                        }
                    };
                }
            };
        }
    }
}