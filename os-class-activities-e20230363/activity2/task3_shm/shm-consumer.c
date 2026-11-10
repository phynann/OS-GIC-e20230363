/* shm-consumer.c — Reads from POSIX shared memory */
#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <unistd.h>

int main(void) {
    const size_t SIZE = 4096;
    const char *name = "/OS-phynann";

    int shm_fd = shm_open(name, O_RDONLY, 0);
    if (shm_fd == -1) {
        perror("shm_open");
        fprintf(stderr, "Hint: Did you run shm-producer first?\n");
        return EXIT_FAILURE;
    }

    char *ptr = mmap(NULL, SIZE, PROT_READ, MAP_SHARED, shm_fd, 0);
    if (ptr == MAP_FAILED) {
        perror("mmap");
        close(shm_fd);
        return EXIT_FAILURE;
    }

    printf("Consumer: reading from shared memory '%s'\n", name);
    printf("Consumer: message = \"%s\"\n", ptr);

    if (munmap(ptr, SIZE) == -1) {
        perror("munmap");
    }
    close(shm_fd);

    if (shm_unlink(name) == -1) {
        perror("shm_unlink");
        return EXIT_FAILURE;
    }
    printf("Consumer: shared memory unlinked.\n");
    return 0;
}
