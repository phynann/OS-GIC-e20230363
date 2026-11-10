/* forkchild.c — Process creation with fork() and exec() */
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>

int main(void) {
    pid_t pid;

    /* Ensure output appears in the expected order when redirected. */
    setbuf(stdout, NULL);

    printf("Parent process (PID: %ld) — creating child...\n", (long)getpid());

    pid = fork();

    if (pid < 0) {
        perror("fork");
        return EXIT_FAILURE;
    } else if (pid == 0) {
        printf("Child process (PID: %ld, Parent PID: %ld)\n",
               (long)getpid(), (long)getppid());
        printf("Child: sleeping 30s — observe the process tree now!\n");
        sleep(30);
        printf("Child: now executing 'ls -la'...\n");

        execlp("ls", "ls", "-la", (char *)NULL);

        /* Reached only if exec fails. */
        perror("execlp");
        _exit(EXIT_FAILURE);
    } else {
        int status;
        printf("Parent: waiting for child (PID: %ld) to finish...\n", (long)pid);

        if (waitpid(pid, &status, 0) == -1) {
            perror("waitpid");
            return EXIT_FAILURE;
        }

        if (WIFEXITED(status)) {
            printf("Parent: child exited with status %d\n", WEXITSTATUS(status));
        } else if (WIFSIGNALED(status)) {
            printf("Parent: child terminated by signal %d\n", WTERMSIG(status));
        }

        printf("Parent: done.\n");
    }

    return 0;
}
