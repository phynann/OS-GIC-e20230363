I need to do Class Activity 3 (Socket Communication & Multithreading) for my OS course.

Spec: https://github.com/RathpiseyAlpha/ITC-I4GIC-OS/blob/main/lectures/class-activity/class-activity3.md
(read the raw version: https://raw.githubusercontent.com/RathpiseyAlpha/ITC-I4GIC-OS/main/lectures/class-activity/class-activity3.md)

Only do the 4 tasks plus the deliverables/README section. Skip the intro and background. Use the spec's code exactly as written.

## Setup
- First, check which OS I'm on (Windows, Linux, macOS, WSL) and which tools are installed (gcc, java/javac, htop, python, etc.). Tell me what's missing before you start.
- Put everything in `activity3/` inside my class-activities folder, using the spec's folder layout exactly: `task1_socket/`, `task2_threads/`, `task3_java/`, `screenshots/`, `README.md`. Ask me for the path if you can't find it.
- My name: <NAME>, student ID: <ID>. Use today's date.

## What to do
- Task 1 (TCP socket in C), Task 2 (pthreads, with and without mutex), Task 3 (Java Thread / Runnable / ExecutorService), Task 4 (watch the threads with OS tools).
- Actually compile and run everything. Save every `result_*.txt` and `command_used_*.txt` file the spec asks for.
- For questions that say "try it" (client before server, removing pthread_join, removing join in Java), really try it on a temporary copy and answer from what actually happened. Don't change the real source files.
- Never make up output, PIDs, thread counts or numbers. Every number in the README must come from a real run, and the README must match the screenshots and the result files.

## Which tools to use on each OS
The C tasks (1, 2, 4A) use POSIX APIs (`arpa/inet.h`, `pthread`, `/proc`), so they need Linux.
- **Linux:** run everything natively. For Task 4B (Windows Task Manager), use real Windows if I have it. If not, Wine can work: run Windows Java under Wine and open Wine's `taskmgr` (tell me first, and say in the README that Wine was used).
- **Windows:** do Tasks 1, 2 and 4A inside **WSL** (Ubuntu). Check that WSL exists with `wsl -l -v` and help me install it if it doesn't (`wsl --install`). Install `gcc`, `htop` and `psmisc` (for `pstree`) inside WSL. Task 3 can run either in WSL or natively on Windows with a Windows JDK. Task 4B uses the real Task Manager natively, with ThreadDemo's sleep raised to 5000 ms on a temporary copy. Note: WSL may hide kernel threads (`kthreadd`, PID 2). If `ps --ppid 2` shows almost nothing, say so in the README as the spec allows. Don't pretend otherwise.
- **macOS:** there is no `/proc` and no `ps -L`/`-eLf`, and kernel threads aren't visible, so the C tasks and 4A should run in a Linux VM or a Docker container (`docker run -it gcc` style, plus `procps` and `htop`). Java can run natively. macOS has no Task Manager, so ask me how I want to handle 4B (another computer, a VM, or noting it's not available).

## Screenshots
Required: task1_socket_exchange, task2_threads_output, task3_java_output, task4_htop_threads, task4_htop_kernel_threads, task4_kernel_threads, task4_taskmanager_threads (all .png in `screenshots/`).
- Each screenshot shows only the command and its output: no window title bar, no desktop. Put related terminals side by side in one image (server | client, threads | threads_mutex, the 3 Java programs).
- Terminal shots: run the real commands, then turn the real output into a terminal-style PNG (for example Python + `pyte` + Pillow, dark background, monospace font). Use my real username/hostname in the prompt.
- htop shots: run real htop in a pseudo-terminal (pyte) and press the keys the spec asks for (F4 filter, H, K, F5 tree; collapse systemd with `-` so `kthreadd` is visible). Give htop its own config folder (`XDG_CONFIG_HOME`) to add the PROCESSOR and NLWP columns, so my real htop settings don't change.
- Real screen capture, if needed: Linux Wayland = `grim` or the compositor's own tool (niri: `niri msg action screenshot-window --id`), Linux X11 = `import`/`scrot`; Windows = PowerShell `System.Drawing` CopyFromScreen, or ask me to press Win+Shift+S; macOS = `screencapture -l <windowid>`.
- If the Task Manager screenshot needs clicks you can't do (Details tab, right-click columns, Threads), tell me exactly what to click and wait, or find a way that doesn't move my mouse. Don't guess.
- Open and look at every screenshot before saying it's done (broken emoji, cut-off lines, leftover redraw garbage).

## Things that went wrong last time
- `ps -eLf | grep threads_observe` also matches the grep itself and the shell running the command. Run Task 4 from a script file and filter those lines out, so the result file only has the 5 real threads.
- `ls` may be aliased (e.g. to eza). Use `/bin/ls` for `/proc/<PID>/task/`.
- Kill any old `threads_observe` before running again, or the extra threads end up in the output.
- `threads_observe` only sleeps for 60 s, so run the observations quickly in one script.
- If anything is run again, update the README numbers to match the new run.

## README
Follow the spec's README template. Answer every question short, simple and casual, like a student wrote it, not AI-sounding. No filler, no overly fancy wording, and don't overdo it. Embed all the screenshots, and paste the `ps` output where the template asks for it. Add a short reflection.

## At the end
Tell me plainly what's done and what's still missing (for example a screenshot I have to take myself).

**Do NOT run `git commit` or `git push` at all, even at the end and even if it seems helpful. I will commit and push myself.**
