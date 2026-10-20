
#include <fcntl.h>    // open(), O_WRONLY, O_CREAT, O_TRUNC
#include <unistd.h>   // write(), close()
#include <string.h>   // strlen()
#include <stdio.h>    // perror()

int main(void) {
    const char *text = "Hello from Operating Systems class!\n";
    const char *msg = "File created successfully!\n";

    // 1. Open or create output.txt
    int fd = open("output.txt", O_WRONLY | O_CREAT | O_TRUNC, 0644);

    if (fd == -1) {
        perror("Error opening file");
        return 1;
    }

    // 2. Write text into the file
    if (write(fd, text, strlen(text)) == -1) {
        perror("Error writing file");
        close(fd);
        return 1;
    }

    // 3. Close the file
    if (close(fd) == -1) {
        perror("Error closing file");
        return 1;
    }

    // 4. Print confirmation to the terminal
    if (write(1, msg, strlen(msg)) == -1) {
        perror("Error printing message");
        return 1;
    }

    return 0;
}
