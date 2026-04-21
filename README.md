# Chess in Java

A two-player chess game built in Java with a graphical interface using Swing.

## Features

- Full chess rules including check, checkmate, and stalemate detection
- Move highlighting — valid moves are shown when a piece is selected
- Revert last move
- Automatic move execution when only one legal move is available

## Requirements

- Java 8 or later

Check your version with:
```bash
java -version
```

## How to Run

### Option 1 — Double-click launcher (Mac)

1. Build the JAR once from the terminal:
```bash
./build.sh
```

2. Double-click `Chess.command` in Finder to launch the game.

> If macOS blocks the file, right-click it and choose **Open**.

---

### Option 2 — Terminal

```bash
cd src
javac *.java
java Grafik
```

You only need to compile once, or again after any code changes.

---

## How to Play

- Click a piece to select it — valid moves will be highlighted
- Click a highlighted square to move the piece
- Use the **Revert Move** button to undo the last move
- The status bar at the top shows whose turn it is, and any check/checkmate/stalemate conditions
