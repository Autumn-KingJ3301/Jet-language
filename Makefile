# Makefile for Jet language project

SRC_DIR=src
BIN_DIR=bin
PACKAGE=com.jet.lang
MAIN_CLASS=Jet
JAVA_FILES=$(SRC_DIR)/com/jet/lang/Jet.java $(SRC_DIR)/com/jet/lang/Scanner.java $(SRC_DIR)/com/jet/lang/Token.java $(SRC_DIR)/com/jet/lang/TokenType.java

.PHONY: all run clean

all:
	javac -d $(BIN_DIR) $(JAVA_FILES)

run: all
	java -cp $(BIN_DIR) $(PACKAGE).$(MAIN_CLASS)

clean:
	rm -rf $(BIN_DIR)/*
