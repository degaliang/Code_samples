#!/bin/bash
# Author: Edward Park
#
# NOTE: This script only runs the acceptance tests that you specify. To run
# all acceptance tests, try "make check". To clean up, try "make clean". For
# further info check the project spec.
#
# DIR: This script should be located in the `proj3/` directory (on the same
# level as the gitlet and testing dirs). You should run this command from the
# same directory.
#
# USAGE: ./gitlet.sh [ARGS] 
# Enter the arguments that you would normally put into the
# "python3 tester.py" command, including flags
#
# MISC: Feel free to change the name of this script!

# CHANGE THIS TO WHATEVER YOUR PYTHON COMMAND IS
PYTHON="python3"

# Filepath options
FP="." # Filepath to the parent of the testing dir
CWD=$(pwd)

# Optional: Flags that you want to include every time, such as --verbose
FLAGS=""

# Compile code
make

# cd into testing dir
cd $FP/testing

# print visual separation
printf "\n-----------------------------------------------------------------\n"

# run tester program
$PYTHON tester.py $FLAGS $@

# cd out
cd CWD
