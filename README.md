# Quick Disk Explorer

## Overview

The Disk Explorer is a quick utility program written in Kotlin that scans and provides information about the files and directories on a system's drives. It uses Kotlin Coroutines to process files efficiently.

## Features

- **Concurrency**: Utilizes Kotlin's coroutines to enhance performance during directory scanning.
- **TreeSet Storage**: Stores the results in a TreeSet ensuring a sorted order based on size, path type, and path.
- **Performance Metrics**: Prints the time taken to process each root directory.
- **Error Handling**: Gracefully handles exceptions that might occur during file access.

## How to Use

1. Compile and run the program.
2. The program will scan all available roots (drives) on your system.
3. Once done, it will print out the total size (in MB) of each directory and its type.

## Classes & Functions

- `PathInfo`: A data class representing a file or directory, its size, type, and any sub-paths. It implements the `Comparable` interface for sorting.
  
  - `PathType`: An enum representing the type of path (`FILE`, `DIRECTORY`, or `UNKNOWN`).
  
- `shouldIgnore(directory: File)`: A function to determine if a specific directory should be ignored during the scan.

- `getDirectoryInfo(directory: File, depth: Int = 0, maxDepth: Int = 100)`: A recursive coroutine function that returns the `PathInfo` of a given directory.

## Output Format

Each line of the output represents a file or directory with the following format:

```
<path>: <size in MB> MB (<type>)
```

For example:

```
C:/Users/John/Documents: 150 MB (DIRECTORY)
```

## Note

If I have the time, I will add a command-line interface to allow the user to specify the root directories to scan.

Certain paths, such as `$Recycle.Bin` and `$WinREAgent`, are ignored during scanning. This is done for efficiency and to avoid scanning unnecessary system files. To modify this behavior, adjust the `shouldIgnore` function.

## License

This project is open source. Feel free to use it however you like.