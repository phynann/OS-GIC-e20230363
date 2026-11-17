Task Overview
Task	What You Do	Where	Key Concepts
Task 1	Build a TCP server and client in C that exchange messages	Linux	socket(), bind(), listen(), accept(), connect(), send(), recv()
Task 2	Write a multithreaded C program using pthreads	Linux	pthread_create(), pthread_join(), thread IDs, shared data
Task 3	Write a multithreaded Java program using Thread and Runnable	Linux/Windows	Thread, Runnable, ExecutorService, synchronized
Task 4	Observe threads at the OS level using ps, htop, Task Manager, and Linux commands that show the kernel's own threads	Linux + Windows	/proc/<pid>/task/, ps -eLf, ps -L, htop (H, K, F5, F4), top -H, kthreadd, pstree, Task Manager
Task 1: TCP Socket Communication (C)
Goal
Write a TCP server and a TCP client in C. The server listens on a port, accepts a connection, reads a message from the client, and sends a response back.

Step-by-Step Instructions
1. Navigate to your task folder
$ cd activity3/task1_socket
2. Create the server
Create a file named server.c:

/* server.c — Simple TCP server */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>

#define PORT 8080
#define BUFFER_SIZE 1024

int main() {
    int server_fd, client_fd;
    struct sockaddr_in address;
    int addrlen = sizeof(address);
    char buffer[BUFFER_SIZE] = {0};
    const char *response = "Hello from server!";

    /* 1. Create socket */
    server_fd = socket(AF_INET, SOCK_STREAM, 0);
    if (server_fd == -1) {
        perror("socket");
        exit(EXIT_FAILURE);
    }
    printf("Server: socket created (fd=%d)\n", server_fd);

    /* Allow port reuse (avoids "Address already in use" error) */
    int opt = 1;
    setsockopt(server_fd, SOL_SOCKET, SO_REUSEADDR, &opt, sizeof(opt));

    /* 2. Bind to address */
    address.sin_family = AF_INET;
    address.sin_addr.s_addr = INADDR_ANY;  /* Listen on all interfaces */
    address.sin_port = htons(PORT);

    if (bind(server_fd, (struct sockaddr *)&address, sizeof(address)) < 0) {
        perror("bind");
        close(server_fd);
        exit(EXIT_FAILURE);
    }
    printf("Server: bound to port %d\n", PORT);

    /* 3. Listen for connections */
    if (listen(server_fd, 1) < 0) {
        perror("listen");
        close(server_fd);
        exit(EXIT_FAILURE);
    }
    printf("Server: listening... waiting for a client to connect.\n");

    /* 4. Accept a connection */
    client_fd = accept(server_fd, (struct sockaddr *)&address, (socklen_t *)&addrlen);
    if (client_fd < 0) {
        perror("accept");
        close(server_fd);
        exit(EXIT_FAILURE);
    }
    printf("Server: client connected!\n");

    /* 5. Receive message from client */
    int bytes_read = read(client_fd, buffer, BUFFER_SIZE - 1);
    if (bytes_read > 0) {
        buffer[bytes_read] = '\0';
        printf("Server: received from client: \"%s\"\n", buffer);
    }

    /* 6. Send response to client */
    send(client_fd, response, strlen(response), 0);
    printf("Server: sent response: \"%s\"\n", response);

    /* 7. Close sockets */
    close(client_fd);
    close(server_fd);
    printf("Server: connection closed.\n");

    return 0;
}
3. Create the client
Create a file named client.c:

/* client.c — Simple TCP client */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>

#define PORT 8080
#define BUFFER_SIZE 1024

int main() {
    int sock_fd;
    struct sockaddr_in server_addr;
    char buffer[BUFFER_SIZE] = {0};
    const char *message = "Hello from client!";

    /* 1. Create socket */
    sock_fd = socket(AF_INET, SOCK_STREAM, 0);
    if (sock_fd == -1) {
        perror("socket");
        exit(EXIT_FAILURE);
    }
    printf("Client: socket created\n");

    /* 2. Set server address */
    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(PORT);

    /* Convert "127.0.0.1" (localhost) to binary form */
    if (inet_pton(AF_INET, "127.0.0.1", &server_addr.sin_addr) <= 0) {
        perror("inet_pton");
        close(sock_fd);
        exit(EXIT_FAILURE);
    }

    /* 3. Connect to server */
    printf("Client: connecting to server on port %d...\n", PORT);
    if (connect(sock_fd, (struct sockaddr *)&server_addr, sizeof(server_addr)) < 0) {
        perror("connect");
        close(sock_fd);
        exit(EXIT_FAILURE);
    }
    printf("Client: connected!\n");

    /* 4. Send message to server */
    send(sock_fd, message, strlen(message), 0);
    printf("Client: sent message: \"%s\"\n", message);

    /* 5. Receive response from server */
    int bytes_read = read(sock_fd, buffer, BUFFER_SIZE - 1);
    if (bytes_read > 0) {
        buffer[bytes_read] = '\0';
        printf("Client: received from server: \"%s\"\n", buffer);
    }

    /* 6. Close socket */
    close(sock_fd);
    printf("Client: connection closed.\n");

    return 0;
}
4. Compile both programs
$ gcc -o server server.c
$ gcc -o client client.c
5. Run the server and client
You need two terminals (or two SSH sessions):

Terminal 1 — Start the server:

$ ./server
The server will print "listening..." and wait for a client.

Terminal 2 — Run the client:

$ ./client
You should see the message exchange:

Server output:

Server: socket created (fd=3)
Server: bound to port 8080
Server: listening... waiting for a client to connect.
Server: client connected!
Server: received from client: "Hello from client!"
Server: sent response: "Hello from server!"
Server: connection closed.
Client output:

Client: socket created
Client: connecting to server on port 8080...
Client: connected!
Client: sent message: "Hello from client!"
Client: received from server: "Hello from server!"
Client: connection closed.
6. Save the output
$ ./server > result_server.txt 2>&1 &
$ sleep 1
$ ./client > result_client.txt 2>&1
$ wait
$ cat result_server.txt result_client.txt > result_socket.txt
7. Save command history
$ history | tail -n 20 > command_used_socket.txt
📸 Required Screenshot: Take a screenshot showing both terminals — server and client — with the message exchange visible.

Questions for Task 1
Answer these in your README.md:

What is the role of bind() in the server? Why doesn't the client call bind()?
What does accept() return? How is it different from the original server socket?
What happens if you start the client before the server? Try it and describe the error.
What does htons(PORT) do? Why is byte order conversion needed?
Draw the sequence of socket calls for both server and client (socket → bind → listen → accept → read/write → close).
Task 2: POSIX Threads on Linux (C)
Goal
Write a C program that creates multiple threads using the pthreads library. Each thread performs a task concurrently, and the main thread waits for all of them to finish.

Step-by-Step Instructions
1. Navigate to your task folder
$ cd ../task2_threads
2. Create the source file
Create a file named threads.c:

/* threads.c — POSIX Threads demonstration */
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <unistd.h>

#define NUM_THREADS 4

/* Shared counter (will demonstrate race condition) */
int shared_counter = 0;

/* Thread function — each thread increments the counter */
void *worker(void *arg) {
    int id = *(int *)arg;
    printf("Thread %d (TID: %lu): starting work...\n", id, (unsigned long)pthread_self());

    /* Simulate work */
    for (int i = 0; i < 100000; i++) {
        shared_counter++;  /* ⚠️ Race condition here! */
    }

    printf("Thread %d (TID: %lu): done.\n", id, (unsigned long)pthread_self());
    pthread_exit(NULL);
}

int main() {
    pthread_t threads[NUM_THREADS];
    int thread_ids[NUM_THREADS];

    printf("Main thread (TID: %lu): creating %d threads...\n",
           (unsigned long)pthread_self(), NUM_THREADS);
    printf("PID: %d — use this to observe threads with ps or htop\n\n", getpid());

    /* Create threads */
    for (int i = 0; i < NUM_THREADS; i++) {
        thread_ids[i] = i + 1;
        if (pthread_create(&threads[i], NULL, worker, &thread_ids[i]) != 0) {
            perror("pthread_create");
            exit(EXIT_FAILURE);
        }
    }

    /* Wait for all threads to finish */
    for (int i = 0; i < NUM_THREADS; i++) {
        pthread_join(threads[i], NULL);
    }

    printf("\nAll threads completed.\n");
    printf("Expected counter value: %d\n", NUM_THREADS * 100000);
    printf("Actual counter value:   %d\n", shared_counter);

    if (shared_counter != NUM_THREADS * 100000) {
        printf("⚠️  Race condition detected! Counter is incorrect.\n");
        printf("This happens because multiple threads modify shared_counter simultaneously.\n");
    }

    return 0;
}
3. Compile with pthreads
$ gcc -o threads threads.c -pthread
⚠️ You must use -pthread flag when compiling pthreads programs.

4. Run the program
$ ./threads > result_threads.txt 2>&1
$ cat result_threads.txt
Run it several times and notice that the final counter value may differ each time (race condition).

5. Fix the race condition with a mutex
Create a file named threads_mutex.c:

/* threads_mutex.c — Threads with mutex synchronization */
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <unistd.h>

#define NUM_THREADS 4

int shared_counter = 0;
pthread_mutex_t counter_mutex = PTHREAD_MUTEX_INITIALIZER;

void *worker(void *arg) {
    int id = *(int *)arg;
    printf("Thread %d (TID: %lu): starting work...\n", id, (unsigned long)pthread_self());

    for (int i = 0; i < 100000; i++) {
        pthread_mutex_lock(&counter_mutex);
        shared_counter++;  /* ✅ Protected by mutex */
        pthread_mutex_unlock(&counter_mutex);
    }

    printf("Thread %d (TID: %lu): done.\n", id, (unsigned long)pthread_self());
    pthread_exit(NULL);
}

int main() {
    pthread_t threads[NUM_THREADS];
    int thread_ids[NUM_THREADS];

    printf("Main thread (TID: %lu): creating %d threads (with mutex)...\n",
           (unsigned long)pthread_self(), NUM_THREADS);

    for (int i = 0; i < NUM_THREADS; i++) {
        thread_ids[i] = i + 1;
        pthread_create(&threads[i], NULL, worker, &thread_ids[i]);
    }

    for (int i = 0; i < NUM_THREADS; i++) {
        pthread_join(threads[i], NULL);
    }

    printf("\nAll threads completed.\n");
    printf("Expected counter value: %d\n", NUM_THREADS * 100000);
    printf("Actual counter value:   %d\n", shared_counter);

    if (shared_counter == NUM_THREADS * 100000) {
        printf("✅ Counter is correct! Mutex prevented the race condition.\n");
    }

    pthread_mutex_destroy(&counter_mutex);
    return 0;
}
6. Compile and run the mutex version
$ gcc -o threads_mutex threads_mutex.c -pthread
$ ./threads_mutex > result_threads_mutex.txt 2>&1
$ cat result_threads_mutex.txt
The counter should now be correct every time.

7. Save command history
$ history | tail -n 20 > command_used_threads.txt
📸 Required Screenshot: Take a screenshot showing the output of both threads (with race condition) and threads_mutex (correct result) side by side.

Questions for Task 2
Answer these in your README.md:

What is a race condition? Why does shared_counter produce an incorrect result in threads.c?
What does pthread_mutex_lock() do? Why does it fix the race condition?
What happens if you forget to call pthread_join()? Try removing it and describe the behavior.
How is a thread different from a process? What do threads share and what is private to each thread?
Task 3: Java Multithreading
Goal
Write a multithreaded Java program using both Thread subclass and Runnable interface approaches, and explore Java's ExecutorService for thread pool management.

Step-by-Step Instructions
1. Navigate to your task folder
$ cd ../task3_java
2. Create ThreadDemo.java — Thread subclass approach
/* ThreadDemo.java — Creating threads by extending Thread */
public class ThreadDemo {

    /* Custom thread class */
    static class CounterThread extends Thread {
        private String name;
        private int count;

        CounterThread(String name, int count) {
            this.name = name;
            this.count = count;
        }

        @Override
        public void run() {
            for (int i = 1; i <= count; i++) {
                System.out.printf("[%s] Count: %d (Thread ID: %d)%n",
                    name, i, Thread.currentThread().getId());
                try {
                    Thread.sleep(500);  /* Simulate work */
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.printf("[%s] Finished!%n", name);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Main thread (ID: " + Thread.currentThread().getId() + "): starting...");
        System.out.println("PID: " + ProcessHandle.current().pid() + " — observe threads with ps or htop\n");

        /* Create and start 3 threads */
        CounterThread t1 = new CounterThread("Alpha", 5);
        CounterThread t2 = new CounterThread("Beta", 5);
        CounterThread t3 = new CounterThread("Gamma", 5);

        t1.start();
        t2.start();
        t3.start();

        /* Wait for all threads to finish */
        t1.join();
        t2.join();
        t3.join();

        System.out.println("\nMain thread: all threads finished.");
    }
}
3. Compile and run
$ javac ThreadDemo.java
$ java ThreadDemo > result_thread_demo.txt 2>&1
$ cat result_thread_demo.txt
Notice how the three threads interleave their output — the OS schedules them concurrently.

4. Create RunnableDemo.java — Runnable interface approach
/* RunnableDemo.java — Creating threads with Runnable */
public class RunnableDemo {

    static class PrintTask implements Runnable {
        private String taskName;

        PrintTask(String name) {
            this.taskName = name;
        }

        @Override
        public void run() {
            for (int i = 1; i <= 3; i++) {
                System.out.printf("[%s] Step %d (Thread: %s, ID: %d)%n",
                    taskName, i, Thread.currentThread().getName(), Thread.currentThread().getId());
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Main: creating threads with Runnable interface\n");

        Thread t1 = new Thread(new PrintTask("Download"), "downloader");
        Thread t2 = new Thread(new PrintTask("Process"), "processor");
        Thread t3 = new Thread(new PrintTask("Upload"), "uploader");

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        System.out.println("\nMain: all tasks completed.");
    }
}
5. Compile and run
$ javac RunnableDemo.java
$ java RunnableDemo > result_runnable_demo.txt 2>&1
$ cat result_runnable_demo.txt
6. Create PoolDemo.java — ExecutorService thread pool
/* PoolDemo.java — Thread pool with ExecutorService */
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PoolDemo {

    static class Task implements Runnable {
        private int taskId;

        Task(int id) {
            this.taskId = id;
        }

        @Override
        public void run() {
            System.out.printf("Task %d started on thread %s (ID: %d)%n",
                taskId, Thread.currentThread().getName(), Thread.currentThread().getId());
            try {
                Thread.sleep(1000);  /* Simulate work */
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("Task %d completed on thread %s%n",
                taskId, Thread.currentThread().getName());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Main: creating thread pool with 2 threads for 6 tasks\n");

        /* Create a fixed pool of 2 threads */
        ExecutorService pool = Executors.newFixedThreadPool(2);

        /* Submit 6 tasks — only 2 run at a time */
        for (int i = 1; i <= 6; i++) {
            pool.submit(new Task(i));
        }

        /* Shut down the pool and wait for completion */
        pool.shutdown();
        pool.awaitTermination(30, TimeUnit.SECONDS);

        System.out.println("\nMain: all tasks completed. Pool shut down.");
    }
}
7. Compile and run
$ javac PoolDemo.java
$ java PoolDemo > result_pool_demo.txt 2>&1
$ cat result_pool_demo.txt
Notice how only 2 tasks run at a time (the pool size), while the other 4 wait in a queue.

8. Save command history
$ history | tail -n 30 > command_used_java.txt
📸 Required Screenshot: Take a screenshot showing the output of ThreadDemo, RunnableDemo, and PoolDemo.

Questions for Task 3
Answer these in your README.md:

What is the difference between extending Thread and implementing Runnable? When would you prefer one over the other?
In PoolDemo, why do only 2 tasks run simultaneously even though 6 are submitted?
What does thread.join() do in Java? What happens if you remove it from ThreadDemo?
Why is ExecutorService considered better than manually creating threads for large applications?
Task 4: Observing Threads in Linux & Windows
Goal
Use OS-level tools to observe threads created by your programs. You will see how the operating system kernel views and schedules threads.

Step-by-Step Instructions
Part A — Observing Threads on Linux
1. Run the threaded C program with a sleep
Modify threads.c (or create threads_observe.c) to add a longer sleep so you have time to observe:

/* threads_observe.c — Threads with sleep for observation */
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <unistd.h>

#define NUM_THREADS 4

void *worker(void *arg) {
    int id = *(int *)arg;
    printf("Thread %d (TID: %lu): sleeping 60 seconds...\n", id, (unsigned long)pthread_self());
    sleep(60);
    printf("Thread %d: done.\n", id);
    return NULL;
}

int main() {
    pthread_t threads[NUM_THREADS];
    int ids[NUM_THREADS];
    printf("PID: %d — observe threads now!\n\n", getpid());

    for (int i = 0; i < NUM_THREADS; i++) {
        ids[i] = i + 1;
        pthread_create(&threads[i], NULL, worker, &ids[i]);
    }
    for (int i = 0; i < NUM_THREADS; i++) {
        pthread_join(threads[i], NULL);
    }
    return 0;
}
$ gcc -o threads_observe threads_observe.c -pthread
$ ./threads_observe &
2. Observe threads with ps
In another terminal:

# Show all threads for the process
$ ps -eLf | grep threads_observe

# Alternative: show threads with LWP (Light Weight Process) IDs
$ ps -T -p <PID>
You should see multiple rows — one for each thread — all sharing the same PID but with different LWP (thread) IDs.

# Save the output
$ ps -eLf | grep threads_observe > result_observe_linux.txt
$ ps -T -p <PID> >> result_observe_linux.txt
3. Observe threads in /proc
# List thread directories (each is a thread's TID)
$ ls /proc/<PID>/task/

# Count threads
$ ls /proc/<PID>/task/ | wc -l
Append to the result file:

$ echo "--- /proc/<PID>/task/ listing ---" >> result_observe_linux.txt
$ ls /proc/<PID>/task/ >> result_observe_linux.txt
4. Observe threads in htop
$ htop
htop is controlled with keys. These are the ones you need in this task:

Key	What it does
H	Show or hide the threads of each process (user threads). Each thread gets its own row, and its thread ID is in the PID column
K	Show or hide the kernel threads
F5 (or t)	Tree view: each process under its parent, so threads and children sit under the process that owns them
F4	Filter the list by a name you type (for example threads_observe). Esc clears the filter
F3	Search for a name and jump to it
F2	Setup. Under Columns, add PROCESSOR (the core a thread last ran on) and NLWP (the number of threads)
F6	Choose the column to sort by
q	Quit
Do this:

Press F4, type threads_observe and press Enter.
Press H until you see five rows: the main thread and the four workers. The main thread's ID is equal to the PID.
Press F2, add the PROCESSOR and NLWP columns, and press Esc. Look at which core each thread is on.
📸 Required Screenshot: Take a screenshot of htop showing the five threads of your program, with the PROCESSOR and NLWP columns.

5. Visualize kernel threads in htop and from the shell
While threads_observe is still running, look at the kernel's own threads. Everything here only reads information. Never send a signal to a kernel thread, and do not press F9 (kill) on one in htop.

(a) In htop: show the kernel threads

Start htop again and clear any filter with F4 and Esc.
Press K to show the kernel threads. If the list does not change, they were already visible: press K again and see which way it goes, then leave them shown.
Press F5 for the tree view. Scroll to the top. You see two families: systemd (PID 1) with all the user programs, and kthreadd (PID 2) with the kernel threads as its children.
Press F3, type kthreadd and press Enter to jump to it. Find at least three of its children, for example ksoftirqd/0, rcu_sched or kworker/....
Press F6, choose a CPU column, and see which kernel threads use the CPU right now. Most of them are asleep and use none.
Now press K again to hide the kernel threads and watch the list shrink. This is the normal view: the kernel's threads are always there, htop just hides them.
📸 Required Screenshot (htop): the tree view with kthreadd (PID 2) and at least three of its children visible.

(b) With commands: the same facts as text you can save

# (1) Your program's threads as the kernel sees them:
#     PID, thread ID (LWP), number of threads (NLWP), CPU core last used (PSR)
$ ps -L -o pid,lwp,nlwp,psr,stat,comm -p <PID>

# (2) The same facts from /proc: one folder per thread, and the thread count
$ ls /proc/<PID>/task/
$ grep -E 'Name|State|Threads' /proc/<PID>/status

# (3) The kernel's own threads: kthreadd is PID 2, and its children are kernel threads
$ ps -o pid,ppid,psr,comm -p 2
$ ps --ppid 2 -o pid,ppid,psr,comm | head -15

# (4) One tree that shows kernel threads and your program together
$ ps -eo pid,ppid,nlwp,comm --forest | head -30
$ pstree -p | head -20

# (5) Optional: the same live view as htop, without htop (press q to quit)
$ top -H -p <PID>
Save the evidence:

$ { ps -L -o pid,lwp,nlwp,psr,stat,comm -p <PID>; ps --ppid 2 -o pid,ppid,psr,comm | head -15; } >> result_observe_linux.txt
(c) Compare what you saw

htop and ps show the same threads: the five rows from step 4 are the five lines of command (1), and the thread IDs match. htop is live and easy to browse, ps gives text you can save and compare.
The main thread's ID is equal to the PID. The other four have their own IDs, and NLWP is 5.
PSR (ps) and PROCESSOR (htop) are the same thing: the core a thread last ran on. Look twice: threads that sleep usually stay on the same core.
Kernel threads have no user address space. ps -ef writes their names in square brackets, for example [kthreadd] or [ksoftirqd/0]. In htop you find them with K and under kthreadd in the tree.
A container or WSL may hide the kernel threads. If K shows almost nothing and command (3) prints almost nothing, say so in your README. Do not conclude that the kernel has no threads.
📸 Required Screenshot (terminal): one terminal showing the output of commands (1) and (3).

6. Clean up
$ kill %1 2>/dev/null  # Kill the background threads_observe process
Part B — Observing Threads on Windows (Java)
1. Run the Java ThreadDemo on Windows
Open a terminal (PowerShell or CMD) on Windows:

javac ThreadDemo.java
java ThreadDemo
While the program is running (the threads sleep for 500ms each step, so you have ~2.5 seconds), observe in Task Manager.

💡 Tip: Increase the sleep time in ThreadDemo.java to 5000 (5 seconds per step) to have more time for observation.

2. Observe threads in Task Manager
Open Task Manager (Ctrl + Shift + Esc)
Go to the Details tab
Find the java.exe process
Right-click the column headers → Select columns → check "Threads"
You should see the thread count for the Java process (it will be more than 3 because the JVM creates internal threads too)
📸 Required Screenshot: Task Manager Details tab showing java.exe with the Threads column visible.

3. Use jstack for detailed thread dump (optional advanced)
While the Java program is running, in another terminal:

jps                    # Find the PID of the Java process
jstack <PID>           # Print thread dump
This shows all threads (including JVM system threads), their states, and stack traces.

Questions for Task 4
Answer these in your README.md:

In the ps -eLf output, what is the LWP column? How does it relate to threads?
How many entries did you find in /proc/<PID>/task/? Does it match the number of threads in your program (including the main thread)?
In Task Manager, why does java.exe show more threads than the 3 you created? What are the extra threads?
Compare how Linux (ps, /proc) and Windows (Task Manager) display threads. Which gives more detail?
What is kthreadd, and what is its PID? Name three kernel threads you found in htop or ps, and say how you can tell that they are kernel threads. (If your system hides them, say so.)
In htop and in the output of ps -L, which thread has an ID equal to the PID, and what is NLWP? On which cores (PSR or PROCESSOR) did your threads run?
What do the keys H and K do in htop? What would you not see without them?
Deliverables & Submission
What to Submit
Everything goes in your README.md — there is no separate answers file. Your README.md must contain:

Your name and student ID
Screenshots of compiling and running each task
Answers to all questions listed under each task
Source code files in the task folders
Output files (.txt files) as specified in each task
Submission Folder Structure
All class activities go inside your personal os-gic-<YourStudentID>/ repository:

os-gic-<YourStudentID>/
├── os-lab-<YourStudentID>/                  # ← Your lab submissions (existing)
│   └── ...
│
└── os-class-activities-<YourStudentID>/     # ← Your class activity submissions
    ├── activity1/                            # ← Activity 1 (already done)
    ├── activity2/                            # ← Activity 2 (already done)
    └── activity3/
        ├── README.md                         # ← Your report: screenshots + answers
        ├── screenshots/                      # Screenshot images
        │   ├── task1_socket_exchange.png
        │   ├── task2_threads_output.png
        │   ├── task3_java_output.png
        │   ├── task4_htop_threads.png
        │   ├── task4_htop_kernel_threads.png
        │   └── task4_taskmanager_threads.png
        ├── task1_socket/
        │   ├── server.c
        │   ├── client.c
        │   ├── result_socket.txt
        │   └── command_used_socket.txt
        ├── task2_threads/
        │   ├── threads.c
        │   ├── threads_mutex.c
        │   ├── threads_observe.c
        │   ├── result_threads.txt
        │   ├── result_threads_mutex.txt
        │   ├── result_observe_linux.txt
        │   └── command_used_threads.txt
        └── task3_java/
            ├── ThreadDemo.java
            ├── RunnableDemo.java
            ├── PoolDemo.java
            ├── result_thread_demo.txt
            ├── result_runnable_demo.txt
            ├── result_pool_demo.txt
            └── command_used_java.txt
Setting Up Your Activity Folder
# Navigate to your existing submission repo
$ cd os-gic-<YourStudentID>

# Create the activity 3 folder structure
$ mkdir -p os-class-activities-<YourStudentID>/activity3/{task1_socket,task2_threads,task3_java,screenshots}

# Start working
$ cd os-class-activities-<YourStudentID>/activity3
Pushing to Git
# Make sure you are in the root of your repo
$ cd os-gic-<YourStudentID>

# Stage your class activity files
$ git add os-class-activities-<YourStudentID>/activity3/

# Commit with a meaningful message
$ git commit -m "Add class activity 3 — Socket Communication & Multithreading"

# Push to your remote repository
$ git push origin main
Reminder: Commit and push regularly as you work — do not wait until the last minute.

README Template for Activity 3
Create a README.md inside your activity3/ folder using this template:

# Class Activity 3 — Socket Communication & Multithreading

- **Student Name:** [Your Name Here]
- **Student ID:** [Your Student ID Here]
- **Date:** [Date of Submission]

---

## Task 1: TCP Socket Communication (C)

### Compilation & Execution

![Socket exchange](screenshots/task1_socket_exchange.png)

### Answers

1. **Role of `bind()` / Why client doesn't call it:**
   > _Your answer_

2. **What `accept()` returns:**
   > _Your answer_

3. **Starting client before server:**
   > _Your answer_

4. **What `htons()` does:**
   > _Your answer_

5. **Socket call sequence diagram:**
   > _Your diagram_

---

## Task 2: POSIX Threads (C)

### Output — Without Mutex (Race Condition)

![Threads output](screenshots/task2_threads_output.png)

### Output — With Mutex (Correct)

_(Include in the same screenshot or a separate one)_

### Answers

1. **What is a race condition?**
   > _Your answer_

2. **What does `pthread_mutex_lock()` do?**
   > _Your answer_

3. **Removing `pthread_join()`:**
   > _Your answer_

4. **Thread vs Process:**
   > _Your answer_

---

## Task 3: Java Multithreading

### ThreadDemo Output

![Java threading](screenshots/task3_java_output.png)

### RunnableDemo Output

_(Include output or screenshot)_

### PoolDemo Output

_(Include output or screenshot)_

### Answers

1. **Thread vs Runnable:**
   > _Your answer_

2. **Pool size limiting concurrency:**
   > _Your answer_

3. **thread.join() in Java:**
   > _Your answer_

4. **ExecutorService advantages:**
   > _Your answer_

---

## Task 4: Observing Threads

### Linux — `ps -eLf` Output

_(Paste the relevant ps output here)_

### Linux — htop Thread View

![htop threads](screenshots/task4_htop_threads.png)

### Linux — htop Kernel Threads (tree view, `kthreadd`)

![htop kernel threads](screenshots/task4_htop_kernel_threads.png)

### Linux — Kernel Threads (commands)

![kernel threads](screenshots/task4_kernel_threads.png)

_(Paste the output of `ps -L ...` and `ps --ppid 2 ...` here)_

### Windows — Task Manager

![Task Manager threads](screenshots/task4_taskmanager_threads.png)

### Answers

1. **LWP column meaning:**
   > _Your answer_

2. **/proc/PID/task/ count:**
   > _Your answer_

3. **Extra Java threads:**
   > _Your answer_

4. **Linux vs Windows thread viewing:**
   > _Your answer_

5. **kthreadd and three kernel threads:**
   > _Your answer_

6. **TID equal to PID, NLWP and cores:**
   > _Your answer_

7. **`H` and `K` in htop:**
   > _Your answer_

---

## Reflection

> _What did you find most interesting about socket communication and threading? How does understanding threads at the OS level help you write better concurrent programs?_
Grading Criteria
Criteria	Points	Description
Task 1 — Socket Communication	25	Server/client compile, run, and exchange messages correctly. Output saved. Questions answered.
Task 2 — POSIX Threads	25	Both versions (race condition + mutex fix) compile and run. Race condition observed and explained. Questions answered.
Task 3 — Java Multithreading	25	All three Java programs compile and run. Thread interleaving and pool behavior observed. Questions answered.
Task 4 — Thread Observation	15	Screenshots of ps, htop (thread view and kernel-thread tree), the kernel-thread commands, and Task Manager showing threads. Questions answered.
README Quality	10	Well-organized README with all screenshots embedded, clear answers, and reflection.
Total	100	
Helpful References
Beej's Guide to Network Programming — Excellent socket programming reference
POSIX Threads Programming — Detailed pthreads tutorial
Java Concurrency Tutorial — Official Java threading guide
man 7 socket — Linux socket manual page
man 7 pthreads — Linux pthreads manual page