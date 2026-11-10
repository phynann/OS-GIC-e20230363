/* winprocess.c — Process creation on Windows using CreateProcess() */
#include <stdio.h>
#include <windows.h>

int main(void) {
    STARTUPINFOA si;
    PROCESS_INFORMATION pi;

    ZeroMemory(&si, sizeof(si));
    si.cb = sizeof(si);
    ZeroMemory(&pi, sizeof(pi));

    printf("Parent process (PID: %lu) — creating child...\n",
           (unsigned long)GetCurrentProcessId());

    if (!CreateProcessA(
            "C:\\Windows\\System32\\mspaint.exe",
            NULL,
            NULL,
            NULL,
            FALSE,
            0,
            NULL,
            NULL,
            &si,
            &pi)) {
        fprintf(stderr, "CreateProcess failed (error %lu).\n",
                (unsigned long)GetLastError());
        return 1;
    }

    printf("Child process created!\n");
    printf("  Child PID:       %lu\n", (unsigned long)pi.dwProcessId);
    printf("  Child Thread ID: %lu\n", (unsigned long)pi.dwThreadId);
    printf("Parent: waiting for the child (mspaint) to exit...\n");
    printf("Open Task Manager and take the required screenshots.\n");
    printf("Close Paint when you are finished observing it.\n");
    fflush(stdout);

    WaitForSingleObject(pi.hProcess, INFINITE);

    printf("Parent: child has exited. Cleaning up.\n");
    CloseHandle(pi.hProcess);
    CloseHandle(pi.hThread);

    return 0;
}
