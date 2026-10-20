
#include <fcntl.h>    // open(), O_RDONLY
#include <unistd.h>   // read(), write(), close()
#include <stdio.h>    // perror()

int main(void) {
    char buffer[256];

    // 1. Open output.txt for reading
    int fd = open("output.txt", O_RDONLY);

    if (fd == -1) {
        perror("Error opening file");
        return 1;
    }

    // 2. Read the file in a loop
    ssize_t bytesRead;

    while ((bytesRead = read(fd, buffer, sizeof(buffer))) > 0) {

        // 3. Display the bytes read on the terminal
        ssize_t totalWritten = 0;

        while (totalWritten < bytesRead) {
            ssize_t n = write(
                1,
                buffer + totalWritten,
                (size_t)(bytesRead - totalWritten)
            );

            if (n <= 0) {
                perror("Error writing to terminal");
                close(fd);
                return 1;
            }

            totalWritten += n;
        }
    }

    if (bytesRead == -1) {
        perror("Error reading file");
        close(fd);
        return 1;
    }

    // 4. Close the file
    if (close(fd) == -1) {
        perror("Error closing file");
        return 1;
    }

    return 0;
}
