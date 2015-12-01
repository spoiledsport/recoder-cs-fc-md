# Intro #
Parsing c`#` source files works best, if files are encoded **ISO-8859-1** with **\r\n** as the line terminator. UTF-8 encoded files will cause trouble when parsing. To ensure files are of the appropriate encoding before parsing, a pre-processing tool was created.

## Tool Usage ##
The tool expects the following command line parameters:
  1. input directory
  1. regEx of files to convert
  1. debug?

**example:**
```

"/Users/janschumacher/tmp/ARTSvn/trunk/ART/src" ".*\.cs$" "true"
```