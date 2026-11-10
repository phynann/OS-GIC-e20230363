/* shm-producer.c — Writes to POSIX shared memory */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <fcntl.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <unistd.h>

int main(void) {
    const size_t SIZE = 4096;
    const char *name = "/OS-phynann";
    const char *message = "Hello, this is shared memory IPC!";

    int shm_fd = shm_open(name, O_CREAT | O_RDWR, 0600);
    if (shm_fd == -1) {
        perror("shm_open");
        return EXIT_FAILURE;
    }

    if (ftruncate(shm_fd, (off_t)SIZE) == -1) {
        perror("ftruncate");
        close(shm_fd);
        return EXIT_FAILURE;
    }

    char *ptr = mmap(NULL, SIZE, PROT_READ | PROT_WRITE, MAP_SHARED, shm_fd, 0);
    if (ptr == MAP_FAILED) {
        perror("mmap");
        close(shm_fd);
        return EXIT_FAILURE;
    }

    snprintf(ptr, SIZE, "%s", message);
    printf("Producer: wrote to shared memory '%s'\n", name);
    printf("Producer: message = \"%s\"\n", message);

    if (munmap(ptr, SIZE) == -1) {
        perror("munmap");
    }
    close(shm_fd);
    return 0;
}
