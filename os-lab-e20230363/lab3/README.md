# Lab 3 — Wildcards, Hard Links and Symbolic Links: Individual Report

- **Student name/ID:** Huong Phynann / e20230363
- **My values (`oslab values lab3`):** name = `rocket`, tag = `draft`, v1 = `404`, v2 = `480`
- **Files:** `evidence/wildcards.txt`, `evidence/links.txt`, `evidence/replacement.txt`, `evidence/checkpoint.txt`

## What I Saw

| State | What `cat` printed for the hard link | What `cat` printed for the symbolic link | What `readlink` printed |
|---|---|---|---|
| Before the rename | `version <v1>` | `version <v1>` | `<name>.txt` |
| After the rename, before the repair | `version <v1>` | Broken link (No such file or directory) | `<name>.txt` |
| After a new file took the old name | `version <v1>` | `version <v2>` | `<name>.txt` |
| After my repair | `version <v1>` | `version <v1>` | `<name>-<tag>.txt` |

**My repair (the two commands I wrote):**

```bash
rm -- "$name-soft.txt"
ln -s "$name-$tag.txt" "$name-soft.txt"
```

## Explanation

When the original file was renamed, the hard link still worked because it pointed to the original inode rather than depending on the filename. The symbolic link became broken because it stored the old filename as its target. When a new file was created using the old filename, the symbolic link started reading `version <v2>` because it followed the new file's path. The hard link continued reading `version <v1>` because it still referenced the original inode. The inode numbers in `evidence/replacement.txt` show that the original file and the replacement file have different inodes.

## What My Tests Do Not Prove

The `cat` command shows the contents of a file but does not tell me whether two filenames share the same inode. I need `ls -li` to compare inode numbers and link counts.

## Plus / Challenge

- **Wildcard patterns:** `[12]` matches exactly one character, either `1` or `2`. `[!1]` matches exactly one character other than `1`, followed by anything matched by `*`.
- **Hard link challenge:** The link count starts at 3, decreases to 2 after deleting `one.txt`, and decreases to 1 after deleting `two.txt`. The data remains accessible until the last hard link is removed and no process still has the file open.

