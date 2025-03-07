"""Wolf and Sheep simulation."""
# Authors: Tycjan Lamkiewicz, Julia Nocuń

import math
import json
import csv
import argparse
import logging
import sys
import os
import random
from configparser import ConfigParser

DEFAULT_NUM_SHEEP = 15
DEFAULT_MAX_ROUNDS = 50
DEFAULT_MEADOW_LIMIT = 10.0
DEFAULT_SHEEP_MOVE_DISTANCE = 0.5
DEFAULT_WOLF_MOVE_DISTANCE = 1.0

script_dir = os.path.dirname(os.path.abspath(__file__))
pos_file_path = os.path.join(script_dir, 'pos.json')
alive_file_path = os.path.join(script_dir, 'alive.csv')
chase_file_path = os.path.join(script_dir, 'chase.log')

class Sheep:
    def __init__(self, id_, meadow_limit, sheep_move_distance):
        if sheep_move_distance <= 0:
            logging.error(f"Invalid sheep move distance {sheep_move_distance}"
                          f"for sheep {id_}. Setting to default.")
            sheep_move_distance = DEFAULT_SHEEP_MOVE_DISTANCE

        self.id = id_
        self.x = random.uniform(-meadow_limit, meadow_limit)
        self.y = random.uniform(-meadow_limit, meadow_limit)
        self.alive = True
        self.sheep_move_distance = sheep_move_distance
        logging.debug(f"Initialized sheep {self.id} at "
                      f"position ({self.x:.3f}, {self.y:.3f}).")

    def move(self):
        if not self.alive:
            return

        direction = random.choice(['N', 'S', 'E', 'W'])
        logging.debug(f"Sheep {self.id} chose direction {direction}.")

        if direction == 'N':
            self.y += self.sheep_move_distance
        elif direction == 'S':
            self.y -= self.sheep_move_distance
        elif direction == 'E':
            self.x += self.sheep_move_distance
        elif direction == 'W':
            self.x -= self.sheep_move_distance

        logging.debug(f"Sheep {self.id} moved to "
                      f"new position ({self.x:.3f}, {self.y:.3f}).")

    def position(self):
        return (self.x, self.y) if self.alive else None


class Wolf:
    def __init__(self, wolf_move_distance):
        self.x = 0.0
        self.y = 0.0
        self.wolf_move_distance = wolf_move_distance
        logging.debug("Wolf initialized at position (0.0, 0.0).")

    def distance_to(self, sheep):
        distance = math.sqrt((self.x - sheep.x) ** 2 + (self.y - sheep.y) ** 2)
        return distance

    def move_towards(self, sheep):
        dx = sheep.x - self.x
        dy = sheep.y - self.y
        distance = math.sqrt(dx ** 2 + dy ** 2)

        if distance <= self.wolf_move_distance:
            # Catch and eat the sheep
            self.x = sheep.x
            self.y = sheep.y
            sheep.alive = False
            logging.info(f"The wolf has eaten sheep {sheep.id}.")
            print(f"The wolf has eaten sheep {sheep.id + 1}.")
        else:
            # Move towards the sheep
            move_ratio = self.wolf_move_distance / distance
            self.x += dx * move_ratio
            self.y += dy * move_ratio
            logging.info(f"The wolf is chasing sheep {sheep.id}.")
            logging.debug(f"The wolf moved to "
                          f"position ({self.x:.3f}, {self.y:.3f}).")
            print(f"The wolf is chasing sheep {sheep.id+1}.")

    def position(self):
        return (self.x, self.y)


def clear_file():

    if os.path.exists(chase_file_path):
        with open(chase_file_path, 'w') as f:
            f.truncate(0)
        logging.debug("Cleared chase.log file.")


class Simulation:
    def __init__(self, num_sheep, max_rounds, meadow_limit,
                 sheep_move_distance, wolf_move_distance, wait_for_keypress):
        self.sheep = [Sheep(i, meadow_limit, sheep_move_distance)
                      for i in range(num_sheep)]
        logging.info(f"Initial positions of all sheep have been determined.")
        self.wolf = Wolf(wolf_move_distance)
        self.rounds = 0
        self.alive_sheep = num_sheep
        self.max_rounds = max_rounds
        self.wait_for_keypress = wait_for_keypress
        self.pos_data = []
        self.alive_data = []

        clear_file()

    def run(self):
        while self.rounds < self.max_rounds and self.alive_sheep > 0:
            self.rounds += 1
            logging.info(f"Starting round {self.rounds}.")
            print(f"Round {self.rounds}:")

            # Sheep move
            for sheep in self.sheep:
                sheep.move()
            logging.info(f"All alive sheep have moved.")

            # Wolf moves
            closest_sheep, closest_distance = self.find_closest_sheep()
            logging.debug(f"Wolf calculated distance to closest "
                          f"sheep {closest_sheep.id} "
                          f"as {closest_distance:.3f}.")
            if closest_sheep:
                self.wolf.move_towards(closest_sheep)
                if not closest_sheep.alive:
                    self.alive_sheep -= 1
            logging.info(f"The wolf moved.")

            # Save data
            self.save_position_data()
            self.save_alive_data()
            logging.info(f"Round {self.rounds} ended "
                         f"with {self.alive_sheep} sheep still alive.")

            # Print round status
            self.print_status()

            if self.wait_for_keypress:
                input("Press Enter to continue...")

        reason = "all sheep have been eaten" \
            if self.alive_sheep == 0 \
            else ("the predefined maximum number "
                  "of rounds has been reached")
        logging.info(f"The simulation has terminated because {reason}.")

    def find_closest_sheep(self):
        closest_sheep = None
        closest_distance = float('inf')
        for sheep in self.sheep:
            if sheep.alive:
                distance = self.wolf.distance_to(sheep)
                if distance < closest_distance:
                    closest_distance = distance
                    closest_sheep = sheep
        return closest_sheep, closest_distance

    def save_position_data(self):
        round_data = {
            'round_no': self.rounds,
            'wolf_pos': self.wolf.position(),
            'sheep_pos': [sheep.position() for sheep in self.sheep]
        }
        self.pos_data.append(round_data)

        try:
            with open(pos_file_path, 'w') as f:
                json.dump(self.pos_data, f, indent=4)
            logging.debug("Position data saved to pos.json.")
        except IOError as e:
            logging.error(f"Failed to save position data to pos.json: {e}")

    def save_alive_data(self):
        self.alive_data.append([self.rounds, self.alive_sheep])

        try:
            with open(alive_file_path, 'w', newline='') as f:
                writer = csv.writer(f)
                writer.writerow(['Round', 'Alive_Sheep'])
                writer.writerows(self.alive_data)
            logging.debug("Alive data saved to alive.csv.")
        except IOError as e:
            logging.error(f"Failed to save alive data to alive.csv: {e}")

    def print_status(self):
        print(f"Wolf position: ({self.wolf.position()[0]:.3f}, "
              f"{self.wolf.position()[1]:.3f})")
        print(f"Alive sheep: {self.alive_sheep}\n")


# Function to load configuration from an INI file
def load_config(filename):
    if not os.path.isfile(filename):
        logging.error(f"File '{filename}' not found.")
        raise FileNotFoundError(f"File '{filename}' does not exist.")

    config = ConfigParser()
    config.read(filename)

    try:
        meadow_limit = config.get('Sheep', 'InitPosLimit')
        sheep_move_distance = config.get('Sheep', 'MoveDist')
        wolf_move_distance = config.get('Wolf', 'MoveDist')

        if (float(meadow_limit) <= 0 or float(sheep_move_distance) <= 0
                or float(wolf_move_distance) <= 0):
            logging.error("Not positive number passed as argument")
            raise ValueError("Not positive number")

        logging.debug(f"Configuration loaded from {filename}: "
                      f"meadow_limit={meadow_limit}, "
                      f"sheep_move_distance={sheep_move_distance}, "
                      f"wolf_move_distance={wolf_move_distance}.")

        return (float(meadow_limit), float(sheep_move_distance),
                float(wolf_move_distance))

    except Exception as e:
        logging.error(f"Error reading configuration "
                      f"from '{filename}': {e}")
        raise

# Argument Parsing
def parse_args():
    parser = argparse.ArgumentParser(description="Wolf and Sheep simulation")
    parser.add_argument('-c', '--config', type=str,
                        help='Configuration file for simulation settings')
    parser.add_argument('-l', '--log', type=str,
                        help='Log level (DEBUG, INFO, WARNING, '
                             'ERROR, CRITICAL)')
    parser.add_argument('-r', '--rounds', type=int,
                        default=DEFAULT_MAX_ROUNDS, help='Number of rounds')
    parser.add_argument('-s', '--sheep', type=int,
                        default=DEFAULT_NUM_SHEEP, help='Number of sheep')
    parser.add_argument('-w', '--wait', action='store_true',
                        help='Wait for key press after each round')

    return parser.parse_args()


def main():
    args = parse_args()

    try:
        if args.sheep <= 0:
            raise ValueError("The number of sheep must be a "
                             "positive integer.")
        if args.rounds <= 0:
            raise ValueError("The number of rounds must be a "
                             "positive integer.")
    except ValueError as e:
        print(f"Argument error: {e}")
        sys.exit(1)

    if args.log:
        log_level = getattr(logging, args.log.upper(), None)
        if log_level is None:
            raise ValueError(f"Invalid log level: {args.log}")
        logging.basicConfig(filename='chase.log', level=log_level,
                            format='%(asctime)s - %(levelname)s - %(message)s')
    else:  # Disable logging if not specified
        logging.basicConfig(level=logging.CRITICAL)

    # Load configuration from file if specified
    (meadow_limit,
     sheep_move_distance,
     wolf_move_distance) = (DEFAULT_MEADOW_LIMIT,
                            DEFAULT_SHEEP_MOVE_DISTANCE,
                            DEFAULT_WOLF_MOVE_DISTANCE)
    if args.config:
        (meadow_limit,
         sheep_move_distance,
         wolf_move_distance) = load_config(args.config)

    # Run the simulation
    simulation = Simulation(num_sheep=args.sheep,
                            max_rounds=args.rounds,
                            meadow_limit=meadow_limit,
                            sheep_move_distance=sheep_move_distance,
                            wolf_move_distance=wolf_move_distance,
                            wait_for_keypress=args.wait)
    simulation.run()


if __name__ == "__main__":
    main()
