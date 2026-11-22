# Lab 4 — Redirection and Pipelines: Individual Report

- **Name:** Huong Phynann
- **Student ID:** e20230363
- **Linux account:** gic-huong-phynann
- **Personal status:** sent
- **Other word:** clear

## Commands I Used

| Stage | My command | Input received | Output produced |
|---|---|---|---|
| Select exact first field | `grep '^sent,' data/events.csv \| tee evidence/selected.txt` | `data/events.csv` | Three matching rows saved and displayed |
| Extract field 2 | `grep '^sent,' data/events.csv \| cut -d, -f2 \| tee evidence/fields.txt` | Matching rows from events.csv | `9`, `4`, `3` |
| Count selected records | `grep '^sent,' data/events.csv \| wc -l > evidence/normal.txt` | Matching rows | Count of `3` |
| Save stdout and stderr separately | `cat data/missing.csv > evidence/stdout.txt 2> evidence/stderr.txt` | Missing file | Empty stdout and error diagnostic in stderr |
| Save exit status immediately | `echo $? > evidence/status.txt` | Previous cat command | Exit status `1` |

## What My Tests Show

| Test | Expected | Observed | Mistake it would catch |
|---|---|---|---|
| Original records | 3 exact matches | 3 records | Incorrect matching or counting |
| No match | 0 records | 0 records | Matching unrelated records |
| One added match plus a look-alike | 4 records | 4 records | Counting quantities instead of records or matching prefixes |
| Missing input | Empty stdout, diagnostic on stderr, nonzero status | Empty stdout, error message on stderr, status 1 | Incorrect redirection or checking the wrong exit status |

## Explain the Commands

**1. Why does the comma in `grep '^WORD,'` matter?**

The comma ensures that grep matches the exact first field. For example, `grep '^sent,'` matches `sent,9` but does not match `sent-late,61` or `not-sent,48`.

**2. Why does `wc -l` count records here, rather than add quantities?**

`wc -l` counts the number of lines in the output. Each matching line represents one record, so it counts records instead of adding the numerical values in the second column.

**3. Where did you use `>>`, and what would `>` do instead?**

I used `>>` when saving the second edge-test count into `evidence/edge.txt`. The first count was saved using `>`. Using `>` again would overwrite the first result instead of appending the second result.

**4. What does `tee` do that `>` alone does not?**

`tee` displays the command output in the terminal while also saving it to a file. In contrast, `>` redirects the output into a file without displaying it in the terminal.

**5. Why must `echo $?` immediately follow the command being checked?**

`$?` stores the exit status of the most recently executed command. If another command runs first, the value changes and may no longer represent the command being checked.

**6. Why can `cat missing-file | wc -l` report pipeline status 0?**

Because the pipeline normally returns the exit status of its last command, which is `wc -l`. Even if `cat` fails, `wc` can successfully count zero input lines and return status 0.

## Limits and Evidence

These commands assume simple comma-separated text without quoted commas. Saved results describe one run; they do not prove every future input works or who typed a command.

The evidence files include:

- `evidence/selected.txt`
- `evidence/fields.txt`
- `evidence/normal.txt`
- `evidence/edge.txt`
- `evidence/stdout.txt`
- `evidence/stderr.txt`
- `evidence/status.txt`
- `evidence/commands.txt`

The checkpoint result is saved in `checkpoint.txt`, beside the evidence directory.

