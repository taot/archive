#! /usr/bin/env bash
find_files() {
  find . -name "*.aux" -or -name "*.log" -or -name "*.odt" -or -name "*.out" -or \
    -name "*.pdf" -or -name "*.tex" -or -name "*.toc" -or -name "*~"
}

find_files
echo -n "Are you sure to remove these files? [Y/n] "
read YES_NO
if [ $YES_NO = 'Y' ] || [ $YES_NO = 'y' ]; then
  find_files | xargs rm -v
fi
