
#include <fcntl.h>
#include <unistd.h>
#include <dirent.h>
#include <sys/stat.h>
#include <string.h>
#include <stdio.h>

int main(void) {
    char buffer[512];

    // 1. Open the current directory
    DIR *dir = opendir(".");

    if (dir == NULL) {
        perror("Error opening directory");
        return 1;
    }

    struct dirent *entry;
    struct stat fileStat;

    // 2. Print the header using write()
    int len = snprintf(
        buffer,
        sizeof(buffer),
        "%-30s %10s\n",
        "Filename",
        "Size (bytes)"
    );
    write(STDOUT_FILENO, buffer, (size_t)len);

    len = snprintf(
        buffer,
        sizeof(buffer),
        "%-30s %10s\n",
        "------------------------------",
        "----------"
    );
    write(STDOUT_FILENO, buffer, (size_t)len);

    // 3. Read directory entries one by one
    while ((entry = readdir(dir)) != NULL) {

        // 4. Get information about the file
        if (stat(entry->d_name, &fileStat) == 0) {

            // 5. Format the filename and size into a string
            int len = snprintf(
                buffer,
                sizeof(buffer),
                "%-30s %10ld\n",
                entry->d_name,
                (long)fileStat.st_size
            );

            // Write the formatted string to the terminal
            if (len > 0 && len < (int)sizeof(buffer)) {
                write(STDOUT_FILENO, buffer, (size_t)len);
            }
        }
    }

    // 6. Close the directory
    closedir(dir);

    return 0;
}
