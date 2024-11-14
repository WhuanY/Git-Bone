# Gitlet Design Document

**Name**: Walter Wu

## Classes

### Blob

A `Blob` instance represents a historical snapshot of a file in the `gitlet` version control system.
Whenever a file in the working directory is **commit after being staged**, `gitlet` creates a new `Blob`
instance for that file, storing the file's content and metadata. Each `Blob` is uniquely identified
and allows `gitlet` to track the evolution of files across commits.

#### Fields

1. `String blobId`: The unique identifier (typically a SHA-1 hash) of the `Blob`. This ID is generated based on the
   file's content and its filename.
2. `String fileName`: The name of the file that this `Blob` represents. It reflects the filename at the time the `Blob`
   was created.
3. `String fileContent`: The actual content of the file when the `Blob` was created. This allows `gitlet` to restore the
   exact state of the file during a checkout operation.

#### Methods

1. `Blob(String id, String fileName, String content)`:  
   Constructor for creating a new `Blob` instance.

   **Parameters:**

    - `id`: The unique identifier for the `Blob`.
    - `fileName`: The name of the file.
    - `content`: The file's content at the time the `Blob` is created.

2. `String getBlobId()`:  
   Returns the unique identifier (`blobId`) of the `Blob`.

   **Return value:**
    - `String`: The `blobId`.

3. `String getFileName()`:  
   Returns the filename that the `Blob` represents.

   **Return value:**
    - `String`: The filename.

4. `String getFileContent()`:  
   Returns the content of the file that the `Blob` represents.

   **Return value:**
    - `String`: The file content.

5. `static Blob blobifyFile(String filename, File f)`:  
   A static method that creates a `Blob` instance from a given file. It reads the content of the file, generates a
   unique identifier based on the filename and content, and returns a new `Blob` instance.

   **Parameters:**
    - `filename`: The name of the file to be blobified.
    - `f`: The `File` object representing the file.

   **Return value:**
    - `Blob`: A new `Blob` instance containing the file's content and metadata.

### Commit

A `Commit` instance represents a snapshot of the project’s state in the `gitlet` version control system. It contains
metadata about the changes captured at a specific point in time, including the commit message, timestamp, and associated
blob references. Each `Commit` can have one or two parent commits, allowing for the representation of both linear
history and merge commits.

#### Fields

1. `String commitId`:  
   The unique identifier for the `Commit`, typically generated using a SHA-1 hash based on the commit's metadata.

2. `long timeStamp`:  
   The time when the commit was created, stored as a long value representing milliseconds since the epoch.

3. `String message`:  
   The descriptive message associated with the commit, explaining the changes or purpose of the commit.

4. `HashMap<String, String> blobs`:  
   A map containing all snapshots of the current version blobs related to the commit. The keys are filenames, and the
   values are the corresponding blob IDs.

5. `String firstParent`:  
   The commit ID of the first parent, representing the immediate predecessor commit.

6. `String secondParent`:  
   The commit ID of the second parent, used in merge commits to reference the other branch being merged.

#### Methods

1. `Commit(String message)`:  
   Constructor for creating a new `Commit` instance with a specified message.

   **Parameters:**

    - `message`: The message describing the changes in the commit.

2. `void resetCommitId()`:  
   Generates and sets the `commitId` based on the current commit’s metadata.

3. `String getMessage()`:  
   Returns the message of the commit.

   **Return value:**

    - `String`: The commit message.

4. `void setMessage(String msg)`:  
   Sets the commit message to the specified value.

   **Parameters:**

    - `msg`: The new message for the commit.

5. `String getCommitId()`:  
   Returns the unique identifier of the commit.

   **Return value:**

    - `String`: The `commitId`.

6. `void setTimeStamp(long timeStamp)`:  
   Sets the timestamp for the commit.

   **Parameters:**

    - `timeStamp`: The time to set for the commit creation.

7. `long getTimeStamp()`:  
   Retrieves the timestamp of the commit.

   **Return value:**

    - `long`: The timestamp.

8. `HashMap<String, String> getBlobs()`:  
   Returns the map of blobs associated with the commit.

   **Return value:**

    - `HashMap<String, String>`: The blobs map.

9. `void addBlob(Blob blob)`:  
   Adds a blob to the commit’s blob map, associating the blob ID with its filename.

   **Parameters:**

    - `blob`: The `Blob` instance to add.

10. `String getFirstParent()`:  
    Returns the commit ID of the first parent.

    **Return value:**

    - `String`: The `firstParent` commit ID.

11. `String getSecondParent()`:  
    Returns the commit ID of the second parent.

    **Return value:**

    - `String`: The `secondParent` commit ID.

12. `void setFirstParent(Commit firstParent)`:  
    Sets the first parent commit ID.

    **Parameters:**

    - `firstParent`: The `Commit` instance to set as the first parent.

13. `void setSecondParent(Commit secondParent)`:  
    Sets the second parent commit ID.

    **Parameters:**

    - `secondParent`: The `Commit` instance to set as the second parent.

14. `void addNewPrecommit(Commit preCommit)`:  
    Adds a new pre-existing commit as a parent, setting the first or second parent as needed.

    **Parameters:**

    - `preCommit`: The `Commit` instance to add as a parent.

15. `void printLog()`:  
    Prints the commit log details including commit ID, timestamp, and message.

### Branch

A `Branch` instance represents a branch in the `gitlet` version control system. Each branch is a reference to a specific
commit within the repository. This allows users to work on different lines of development in parallel.

#### Fields

1. `String name`:  
   The name of the branch. This is used to uniquely identify the branch within the repository.

2. `String ref`:  
   The reference to the commit that the branch currently points to. This typically represents the commit ID.

#### Methods

1. `Branch(String branchName, String branchRef)`:  
   Constructor for creating a new `Branch` instance.

   **Parameters:**

    - `branchName`: The name of the branch.
    - `branchRef`: The commit reference that the branch points to.

2. `String getName()`:  
   Returns the name of the branch.

   **Return value:**

    - `String`: The branch name.

3. `String getRef()`:  
   Returns the commit reference that the branch points to.

   **Return value:**

    - `String`: The commit reference.

4. `void setRef(String newref)`:  
   Updates the commit reference that the branch points to.

   **Parameters:**

    - `newref`: The new commit reference.

5. `static void setInitHead(String newHeadName)`:  
   A static method that initializes the head of the repository to a specified branch name. It creates the head state
   file if it does not exist and writes the new head name to it.

   **Parameters:**

    - `newHeadName`: The name of the new head branch

## Data Structure

Because the two main differences between gitlet and real gits are:

```txt
1. Each Commit will at most has TWO parents (due to merge opreation).
2. Working Directory only supports files(i.e.: no subfolder)
```

Therefore, the implementation in data structure in gitlet can be simplified. Here are __ main data structures used in
gitlet.

### Global `Commits` Relationship-DAG

The uppermost structure for the whole project(i.e.: collections of node representing single commit) can be seen as a *
*Directed Acyclic Graph**.
Unlike using **adjacency matrix** or **adjacency list** to represents the links between each node, connectivity between
two nodes is only implemeted by field `firstParent` and `secondParent` in `Commit` class.

### `Commits` in a `branch`-SLL

Feature commands in gitlet that only requires getting **continuous** commit info is `log`. It requires to print all
historical commits from current commit, but **only track previous commits by first parent!**
First parent with values containing a specific commit id, together to make each commit the start of a single linked
list(SLL).

### `Blobs` infos in a `Commit`- HashTable

Opearations like checkout a file needs to search for a file snapshot in a commit. Using hashtable for quick blob info
retrival.

## Persistence

Files needed to maintain state of the program all stored in `.gitlet` repository:

```
CWD/              <==== Whatever the current working directory is.
├─ gitlet/         <==== All persistant data is stored within here.
│  ├─ blobs/       <==== All blobs object is stored within here.
│  │  ├─ blobId0
│  │  ├─ ...
│  ├─ branch/      <==== All branch object is stored within here.
│  │  ├─ HEAD
│  │  ├─ branchName0
│  │  ├─ branchName1
│  │  ├─ ...
│  ├─ commits/     <==== All commit object is stored within here.
│  │  ├─ commitId0
│  │  ├─ commitId1
│  │  ├─ ...
│  ├─ stage_add/                     <==== Stage Add Area
│  ├─ stage_remove/                  <==== Stage Add Area
├─ workdingDirectoryFiles            <==== Other Files 
```
