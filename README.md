[![Build Status](https://travis-ci.org/wte4j/wte4j.svg?branch=master)](https://travis-ci.org/wte4j/wte4j)

# wte4j - word template engine for java

## What is wte4j?
Wte4j is a Spring component that generates Open XML documents from a template.
As a template one can use .docx or .dotx files with content-controls used by Microsoft Word as placeholders for the dynamic content.

## What do you get?
Wte4j consists of five modules:

- **wte4j-core** is the template engine, usable as a Spring component
- **wte4j-admin** is a web fragment for managing the templates in a repository of a wte4j instance (not packed in release)
- **wte4j-admin-auth** is a web fragment that implements an authentication solution (not packed in release)
- **wte4j-admin-war** is a web app that combines **wte4j-admin** and **wte4j-admin-auth**
- **wte4j-showcase** is a small demo web app that uses **wte4j-core** and **wte4j-admin** to give a functional overview of wte4j

## How do you run wte4j-showcase?
You can find the latest release of the wte4j repo archived as .zip and .tar.gz files in the "releases" area of this Github repo:

https://github.com/wte4j/wte4j/releases

1. Download Tomcat 7 from http://tomcat.apache.org/ (min Version 7) and unarchive it
2. Download a .zip or .tar.gz release, unarchive it.
3. Copy the wars/wte4j-showcase.war file in tomcats webapp folder
4. Start tomcat. Showcase will be available at http://localhost:8080/wte4j-showcase

For a more detailed documentation visit this project's wiki here: https://github.com/wte4j/wte4j/wiki
