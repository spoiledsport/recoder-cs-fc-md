#!/bin/bash

echo "Generating Parser tests"

if [ "$1" != "" ]
then
	outfile="$1"
else
	echo "Usage: $0 <outfile> <filenameprefix>"
	echo "Example: $0 tests \"Mono/\""
	echo "This way tests generated in multiple directories can be merged together."
	exit 1
fi

if  [ "$2" != "" ] 
then
	filenameprefix=$2
fi

rm -f $outfile

for filename in *.cs 
do
testname=${filename%.cs}

testname=$(echo $testname | sed 's/-//g')

echo $testname

testnameprefix=$( echo $filenameprefix | sed 's#/##g')

cat >>$outfile <<EOF

public void test$testnameprefix$testname() throws ParserException, IOException {
		parserTest(testDir+"/"+"$filenameprefix$filename");
}


EOF

done

