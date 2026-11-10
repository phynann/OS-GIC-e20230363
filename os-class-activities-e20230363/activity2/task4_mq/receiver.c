/* receiver.c — Receives a message from a POSIX message queue */
#include <stdio.h>
#include <stdlib.h>
#include <mqueue.h>
#include <fcntl.h>
#include "common.h"

int main(void) {
    mqd_t mq;
    struct mq_attr attr;
    char buffer[MAX_SIZE + 1] = {0};

    mq = mq_open(QUEUE_NAME, O_RDONLY);
    if (mq == (mqd_t)-1) {
        perror("mq_open");
        fprintf(stderr, "Hint: Did you run sender first?\n");
        return EXIT_FAILURE;
    }

    if (mq_getattr(mq, &attr) == -1) {
        perror("mq_getattr");
        mq_close(mq);
        return EXIT_FAILURE;
    }

    if (mq_receive(mq, buffer, attr.mq_msgsize, NULL) == -1) {
        perror("mq_receive");
        mq_close(mq);
        return EXIT_FAILURE;
    }

    printf("Receiver: message received from queue '%s'\n", QUEUE_NAME);
    printf("Receiver: message = \"%s\"\n", buffer);

    if (mq_close(mq) == -1) {
        perror("mq_close");
        return EXIT_FAILURE;
    }
    if (mq_unlink(QUEUE_NAME) == -1) {
        perror("mq_unlink");
        return EXIT_FAILURE;
    }
    printf("Receiver: queue unlinked.\n");
    return 0;
}
