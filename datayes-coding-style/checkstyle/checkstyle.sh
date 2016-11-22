#! /bin/bash

DIR=$(dirname $(readlink -f $0))
SRC=$(readlink -f $1)
java -jar -Dstylecheck.dir="$DIR" -Dbasedir="$SRC" "$DIR/checkstyle-5.6-all.jar" \
    -c "$DIR/datayes-checkstyle.xml" -r $@
RET=$?
if [ $RET == 0 ]; then
    echo "Style check passed."
else
    echo "Style check failed."
fi
exit $RET
