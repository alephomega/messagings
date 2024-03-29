package com.xxx.messaging;

import sun.net.www.protocol.file.FileURLConnection;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class PackageScanner {

    static List<Class> getSubTypesOf(String packageName, Class<?> type) {
        try {
            return getClassesForPackage(packageName).stream().filter(type::isAssignableFrom).collect(Collectors.toList());
        } catch (ClassNotFoundException e) {
            return Collections.emptyList();
        }
    }

    private static void checkDirectory(File directory, String packageName, ArrayList<Class<?>> classes) throws ClassNotFoundException {
        File tmpDirectory;

        if (directory.exists() && directory.isDirectory()) {
            final String[] files = directory.list();

            for (final String file : files) {
                if (file.endsWith(".class")) {
                    try {
                        classes.add(Class.forName(packageName + '.' + file.substring(0, file.length() - 6)));
                    } catch (final NoClassDefFoundError e) {

                    }
                } else if ((tmpDirectory = new File(directory, file)).isDirectory()) {
                    checkDirectory(tmpDirectory, packageName + "." + file, classes);
                }
            }
        }
    }

    private static void checkJarFile(JarURLConnection connection, String packageName, ArrayList<Class<?>> classes) throws ClassNotFoundException, IOException {
        JarFile jarFile = connection.getJarFile();
        Enumeration<JarEntry> entries = jarFile.entries();

        String name;
        for (JarEntry jarEntry; entries.hasMoreElements() && ((jarEntry = entries.nextElement()) != null);) {
            name = jarEntry.getName();

            if (name.contains(".class")) {
                name = name.substring(0, name.length() - 6).replace('/', '.');

                if (name.contains(packageName)) {
                    classes.add(Class.forName(name));
                }
            }
        }
    }

    public static ArrayList<Class<?>> getClassesForPackage(String packageName) throws ClassNotFoundException {
        final ArrayList<Class<?>> classes = new ArrayList<>();

        try {
            final ClassLoader cld = Thread.currentThread().getContextClassLoader();

            if (cld == null) {
                throw new ClassNotFoundException("Can't get class loader.");
            }

            final Enumeration<URL> resources = cld.getResources(packageName.replace('.', '/'));
            URLConnection connection;

            for (URL url; resources.hasMoreElements() && ((url = resources.nextElement()) != null);) {
                try {
                    connection = url.openConnection();

                    if (connection instanceof JarURLConnection) {
                        checkJarFile((JarURLConnection) connection, packageName, classes);
                    } else if (connection instanceof FileURLConnection) {
                        try {
                            checkDirectory(new File(URLDecoder.decode(url.getPath(), "UTF-8")), packageName, classes);
                        } catch (final UnsupportedEncodingException ex) {
                            throw new ClassNotFoundException(packageName + " does not appear to be a valid package (Unsupported encoding)", ex);
                        }
                    } else {
                        throw new ClassNotFoundException(packageName + " (" + url.getPath() + ") does not appear to be a valid package");
                    }

                } catch (final IOException ioe) {
                    throw new ClassNotFoundException("IOException was thrown when trying to get all resources for " + packageName, ioe);
                }
            }
        } catch (final NullPointerException ex) {
            throw new ClassNotFoundException(packageName + " does not appear to be a valid package (Null pointer exception)", ex);
        } catch (final IOException ioe) {
            throw new ClassNotFoundException("IOException was thrown when trying to get all resources for " + packageName, ioe);
        }

        return classes;
    }
}
