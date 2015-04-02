# wte4j - word template engine for java

## What is wte4j?
The wte4j is a Spring component that generates Open XML documents from a template.
As a template one can use .docx or .dotx files with content-controls used by Microsoft Word as placeholders for the dynamic content.

## What do you get?
The wte4j consists of five modules:

- **wte4j-core** is the template engine, usable as a Spring component
- **wte4j-admin** is a web fragment for managing the templates in a repository of a wte4j instance
- **wte4j-admin-auth** is a web fragment that implements an authentication solution
- **wte4j-admin-war** is a web app that combines **wte4j-admin** and **wte4j-admin-auth**
- **wte4j-showcase** is a small demo web app that uses **wte4j-core** and **wte4j-admin** to give a functional overview of wte4j

## How to run the wte4j-showcase?
You can find the latest release of the wte4j repo as .zip and .tar.gz files in the "releases" area of this Github repo:

https://github.com/wte4j/wte4j/releases

Download a .zip or .tar.gz release, unarchive it, then following instructions in the wte4j-showcase README file.

For a more detailed documentation visit the projects wiki here: https://github.com/wte4j/wte4j/wiki
