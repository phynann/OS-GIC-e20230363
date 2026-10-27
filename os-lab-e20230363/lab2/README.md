# Lab 2 — Linux Navigation and File Management: Individual Report

- **Student name/ID:** HUONG PHYNANN / e20230363
- **Ubuntu username:** `gic-huong-phynann`
- **My values (`oslab values lab2`):** file1 = `payroll may.txt`, file2 = `vendor list.txt`, owner = `Sales`, reader = `Marketing`, third = `HR`
- **Files:** `evidence/paths.txt`, `evidence/tree.txt`, `evidence/checkpoint.txt`

## What I Did

| Step | My command | Current folder when I ran it |
|---|---|---|
| Move the first file (relative destination) | `mv "incoming/payroll may.txt" reports/` | `~/oslab-work/lab2` |
| Move the second file (absolute paths) | `mv "/home/gic-huong-phynann/oslab-work/lab2/incoming/vendor list.txt" "/home/gic-huong-phynann/oslab-work/lab2/reports/"` | `~/oslab-work/lab2` |
| Read the report from my reader department (relative path) | `cat "../Sales/payroll may.txt"` | `~/oslab-work/lab2/TechCorp/Marketing` |
| The path error I made on purpose, and its message | `cat "./Sales/payroll may.txt"` — `No such file or directory` | `~/oslab-work/lab2/TechCorp/Marketing` |
| The relative path that fixed it | `cat "../../reports/payroll may.txt"` — `Fix: ../../reports/payroll may.txt` | `~/oslab-work/lab2/TechCorp/Marketing` |

## Explanation

A correct file name can still give "No such file or directory" when I use the wrong path or run the command from a different working directory. A relative path starts from my current directory, while an absolute path starts from the root directory `/`. I used `mv` to move `payroll may.txt` from `incoming` to `reports`, changing its location without keeping the original in `incoming`. I used `cp` to make a separate copy of the report in `TechCorp/Sales/archive/first-original.txt`, so the original report remained available. Quoting file names containing spaces prevents the shell from treating the words as separate arguments.

## What My Tests Do Not Prove

`ls` shows file names and directory entries, but it does not prove that two files contain identical data. `cmp` compares file contents byte by byte, but a successful comparison does not prove that the file is stored in the correct location or that its permissions are appropriate.

## Plus / Challenge (only if you did them)

- **Plus:** The relative path `../../reports/payroll may.txt` uses two `..` components to move up two directory levels before entering `reports`.
- **Challenge:** `cp reports/*.txt TechCorp/HR/` copies matching non-hidden `.txt` files from `reports` into `TechCorp/HR/`. The wildcard `*` does not normally match hidden file names beginning with `.`.
