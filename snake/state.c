#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "snake_utils.h"
#include "state.h"

/* Helper function definitions */
static char get_board_at(game_state_t* state, int x, int y);
static void set_board_at(game_state_t* state, int x, int y, char ch);
static bool is_tail(char c);
static bool is_snake(char c);
static char body_to_tail(char c);
static int incr_x(char c);
static int incr_y(char c);
static void find_head(game_state_t* state, int snum);
static char next_square(game_state_t* state, int snum);
static void update_tail(game_state_t* state, int snum);
static void update_head(game_state_t* state, int snum);

/* Helper function to get a character from the board (already implemented for you). */
static char get_board_at(game_state_t* state, int x, int y) {
  return state->board[y][x];
}

/* Helper function to set a character on the board (already implemented for you). */
static void set_board_at(game_state_t* state, int x, int y, char ch) {
  state->board[y][x] = ch;
}

/* Task 1 */
game_state_t* create_default_state() {
  
  game_state_t* new_game = malloc(sizeof(game_state_t)); // Allocate memory for the game_state struct

  //Initialize game board size
  new_game->x_size = 14;
  new_game->y_size = 10;

  // Allocate memory for board
  new_game->board = malloc(new_game->y_size * sizeof(char*));
  for (int i = 0; i < new_game->y_size; i++) {
    new_game->board[i] = malloc((new_game->x_size + 1) * sizeof(char));
  }

  // Initializing the board array
  strcpy(new_game->board[0], "##############");
  strcpy(new_game->board[1], "#            #");
  strcpy(new_game->board[2], "#        *   #");
  strcpy(new_game->board[3], "#            #");
  strcpy(new_game->board[4], "#   d>       #");
  strcpy(new_game->board[5], "#            #");
  strcpy(new_game->board[6], "#            #");
  strcpy(new_game->board[7], "#            #");
  strcpy(new_game->board[8], "#            #");
  strcpy(new_game->board[9], "##############");

  new_game->num_snakes = 1; // Setting number of snake to 1

  new_game->snakes = malloc(new_game->num_snakes * sizeof(snake_t)); // Allocate memory for snakes, the number is 1 as default

  // Initialize snake
  snake_t* new_snake = new_game->snakes;
  new_snake->tail_x = 4;
  new_snake->tail_y = 4;
  new_snake->head_x = 5;
  new_snake->head_y = 4;
  new_snake->live = true;


  return new_game;
}

/* Task 2 */
void free_state(game_state_t* state) {
  // Free the board
  char **board = state->board;
  unsigned int board_size = state->y_size;
  for (int i = 0; i < board_size; i++) {
    free(board[i]);
  }
  free(board);
  
  free(state->snakes); // Free snakes

  free(state); // Free game_state
  return;
}

/* Task 3 */
void print_board(game_state_t* state, FILE* fp) {
  // TODO: Implement this function.
  return;
}

/* Saves the current state into filename (already implemented for you). */
void save_board(game_state_t* state, char* filename) {
  FILE* f = fopen(filename, "w");
  print_board(state, f);
  fclose(f);
}

/* Task 4.1 */
static bool is_tail(char c) {
  // TODO: Implement this function.
  return true;
}

static bool is_snake(char c) {
  // TODO: Implement this function.
  return true;
}

static char body_to_tail(char c) {
  // TODO: Implement this function.
  return '?';
}

static int incr_x(char c) {
  // TODO: Implement this function.
  return 0;
}

static int incr_y(char c) {
  // TODO: Implement this function.
  return 0;
}

/* Task 4.2 */
static char next_square(game_state_t* state, int snum) {
  // TODO: Implement this function.
  return '?';
}

/* Task 4.3 */
static void update_head(game_state_t* state, int snum) {
  // TODO: Implement this function.
  return;
}

/* Task 4.4 */
static void update_tail(game_state_t* state, int snum) {
  // TODO: Implement this function.
  return;
}

/* Task 4.5 */
void update_state(game_state_t* state, int (*add_food)(game_state_t* state)) {
  // TODO: Implement this function.
  return;
}

/* Task 5 */
game_state_t* load_board(char* filename) {
  // TODO: Implement this function.
  return NULL;
}

/* Task 6.1 */
static void find_head(game_state_t* state, int snum) {
  // TODO: Implement this function.
  return;
}

/* Task 6.2 */
game_state_t* initialize_snakes(game_state_t* state) {
  // TODO: Implement this function.
  return NULL;
}
