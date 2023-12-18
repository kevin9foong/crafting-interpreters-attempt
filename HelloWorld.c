#include <stdio.h>

int main() {
    // c-strings are \0 terminated
    // c compiler adds \0 to end of string when using ""
    // char* name = "Kevin"; -> immutable since string literals are stored in read-only portion of memory,
    //  compiler knows the length
    // char name[] = "Kevin_2"; -> mutable, compiler knows the length
    char* name = "Kevin";

    printf("Hello world, %s\n", name);
}

// To compile: gcc -o output_file input_file.c

