[Learn more about Brave Clojure On-Site Training](http://www.braveclojure.com/training/)

## Movement

|Keys	 | Description |
|--------|-------------|
|C-a	 | Move to beginning of line. |
|M-m	 | Move to first non-whitespace character on the line. |
|C-e	 | Move to end of line. |
|C-f	 | Move forward one character. |
|C-b	 | Move backward one character. |
|M-f	 | Move forward one word (I use this a lot). |
|M-b	 | Move backward one word (I use this a lot, too). |
|C-s	 | Regex search for text in current buffer and move to it. Press C-s again to move to next match. |
|C-r	 | Same as C-s, but search in reverse. |
|M-<	 | Move to beginning of buffer. |
|M->	 | Move to end of buffer. |
|M-g g	 | Go to line. |


## Killing and yanking

| Keys	| Description |
| ----- | ------------|
| C-w	| Kill region. |
| M-w	| Copy region to kill ring. |
| C-y	| Yank. |
| M-y	| Cycle through kill ring after yanking. |
| M-d	| Kill word. |
| C-k	| Kill line. |


## Getting Help

| Keys	| Description |
| ----- | ------------|
| C-h k | key-binding	Describe the function bound to the key binding. To get this to work, you actually perform the key sequence after typing C-h k. |
| C-h f	| Describe function. |


## Working with frames and windows

| Keys	| Description |
| ----- | ------------|
| C-x o	| Switch cursor to another window. Try this now to switch between your Clojure file and the REPL. |
| C-x 1	| Delete all other windows, leaving only the current window in the frame. This doesn’t close your buffers, and it won’t cause you to lose any work. |
| C-x 2	| Split frame above and below. |
| C-x 3	| Split frame side by side. |
| C-x 0	| Delete current window. |

## Clojure Buffer key bindings

| Keys	        | Description |
| ----- | ------------|
| C-c M-n	    | Switch to namespace of current buffer. |
| C-x C-e	    | Evaluate expression immediately preceding point. |
| C-c C-k	    | Compile current buffer. |
| C-c C-d C-d	| Display documentation for symbol under point. |
| M-. and M-,	| Navigate to source code for symbol under point and return to your original buffer. |
| C-c C-d C-a	| Apropros search; find arbitrary text across function names and documentation. |

## CIDER buffer key bindings

| Keys	    | Description |
| ----------|------------ |
| C-↑, C-↓	| Cycle through REPL history. |
| C-enter	| Close parentheses and evaluate. |

## Paredit key bindings

| Keys | Description |
| ----- | ------------|
| M-x paredit-mode	| Toggle paredit mode. |
| M-(	| Surround expression after point in parentheses (paredit-wrap-round). |
| C-→	| Slurp; move closing parenthesis to the right to include next expression. |
| C-←	| Barf; move closing parenthesis to the left to exclude last expression. |
| C-M-f, C-M-b	| Move to the opening/closing parenthesis. |
