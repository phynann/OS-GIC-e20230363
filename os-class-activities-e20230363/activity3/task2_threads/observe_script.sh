#!/bin/bash
PID=71463
echo '--- ps -eLf | grep [t]hreads_observe ---'
ps -eLf | grep '[t]hreads_observe'
echo '--- ps -T -p 71463 ---'
ps -T -p "$PID"
echo '--- /bin/ls /proc/71463/task/ ---'
/bin/ls /proc/"$PID"/task/
echo '--- grep -E '\''Name|State|Threads'\'' /proc/71463/status ---'
grep -E 'Name|State|Threads' /proc/"$PID"/status
echo '--- ps -L -o pid,lwp,nlwp,psr,stat,comm -p 71463 ---'
ps -L -o pid,lwp,nlwp,psr,stat,comm -p "$PID"
echo '--- ps --ppid 2 -o pid,ppid,psr,comm | head -15 ---'
ps --ppid 2 -o pid,ppid,psr,comm | head -15
