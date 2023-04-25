#!/bin/bash

cd ./target/universal/stage/bin
sed -i "s/\(set .CFG_FILE=%DEBQSUDAMERIS_HOME%\).*\(.DEBQSUDAMERIS_config.txt\)/\1\\\.\.\2/" debqsudameris.bat
sed -i "s@\(if not defined JAVACINSTALLED set JAVAOK=false\)@rem \1@" debqsudameris.bat
sed -i 's@\("%_JAVACMD%" %_JAVA_OPTS% %DEBQSUDAMERIS_OPTS% -cp \).*\( %APP_MAIN_CLASS% %CMDS%\)@\1"%APP_LIB_DIR%/*"\2@' debqsudameris.bat
sed -i "/set .APP_CLASSPATH=.*/d" debqsudameris.bat
cd ../../../../
