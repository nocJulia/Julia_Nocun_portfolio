#Autorzy: Łukasz Gałuszewski 247660, Julia Nocuń 247744

import sys
import row_cols
import BFSalgorithm  
import DFSalgorithm 
import AstarAlgorithm
import board

if len(sys.argv) != 6:                      #sprawdzamy czy program został wywołany z odpowiednia ilością argumentów
        print("Wrong number of arguments!") #jeśli podano nieprawidlowa ilosc argumentow to wyswietlamy komunikat i konczymy dzialanie programu
        sys.exit(1)
else:
    strategy = sys.argv[1]                  #przypisujemy do zmiennych wartości poszczególnych argumentów
    param = sys.argv[2]                     #tak, aby łatwiej było nam korzystać z nich w dalszej części programu 
    board_file = sys.argv[3]
    solution_file = sys.argv[4]
    statistics_file = sys.argv[5]

    #wczytujemy plansze z pliku i umieszczamy wartości w tablicy
    with open(board_file, 'r') as start_board_file:
        size = start_board_file.readline().strip().split()  
        row_cols.ROWS, row_cols.COLUMNS = map(int, size)  
        board_array = []
        for _ in range(row_cols.ROWS):  #iterujac przez wiersze odczytujemy ich wartosci z pliku i przypisujemy do zmiennej
            row = list(map(int, start_board_file.readline().strip().split()))  
            board_array.append(row)
        start_board_file.close()
    
    start_board = board.Board(board_array, None, None, 0)   #na podstawie wartosci wczytanych z pliku tworzymy plansze startowa

    #Tworzymy planszę docelową. Pętla zewnętrzna iteruje po liczbie wierszy planszy,a pętla wewnętrzna iteruje po 
    #liczbie kolumn planszy. W pętli wewnętrznej, dla każdej komórki, obliczana jest wartość komórki w celu wypełnienia  
    #planszy zgodnie z regułą, że każda komórka otrzymuje kolejną liczbę zaczynając od 1, a ostatnia komórka otrzymuje wartość 0. 
    #ze względu na to, że program ma być uniwerslany dla różnych wielkości planszy nie możemy na "sztywno" wpisać docelowej planszy

    goal_board = []    
    goal_board = [[i * row_cols.COLUMNS + j + 1 for j in range(row_cols.COLUMNS)] for i in range(row_cols.ROWS)]
    goal_board[row_cols.ROWS - 1][row_cols.COLUMNS - 1] = 0

    row_cols.SOLVED_BOARD = board.Board(goal_board, None, None, None)
    
    if strategy == 'bfs':  #strategia "wszerz" (breadth-first search)
        result = BFSalgorithm.bfs(board, start_board, param)
    elif strategy == 'dfs':  #strategia "w głąb" (depth-first search)
        result = DFSalgorithm.dfs(board, start_board, param)
    else:  #strategia A* (A-star)
        if param == 'hamm':  #heurystyka - metryka Hamminga
            result = AstarAlgorithm.astar(board, start_board, AstarAlgorithm.hamming_heuristic)
        else:  #heurystyka - metryka Manhattan
            result = AstarAlgorithm.astar(board, start_board, AstarAlgorithm.manhatan_heuristic)

    #poniżej zapis do plików

    solution, visited_states, processed_states, max_recursion_depth, duration = result
    solution_stat = solution

#plik z rozwiazaniem
    with open(solution_file, "w") as file_sol:
        if solution is None:        #zgodnie z wymaganiami, jesli nie znaleziono rozwiazania do pliku zapisujemy wartosc -1
            file_sol.write("-1")
        else:                       #w przeciwnym razie do momentu gdy istnieje rodzic rozwiazania, zwiększamy jego długosc o 1 oraz 
            length_solution = 0     #dodajemy kolejno wykonywane ruchy do sciezki
            path = ""

            while solution.parent is not None:
                length_solution += 1
                path += solution.path
                solution = solution.parent

            path = path[::-1]       #aby uzyskać prawidlowa kolejnosc musimy odwrocic kolejnosc ruchów w ścieżce, a nastepnie zapisac do pliku dł i kolejnosc rozwiazania
            file_sol.write(str(length_solution)+'\n')
            file_sol.write(path)
        file_sol.close()

#plik ze statystykami
#1 linia - długość znalezionego rozwiązania - o takiej samej wartości jak w pliku z rozwiązaniem
#2 linia - liczba stanów odwiedzonych
#3 linia - liczba stanów przetworzonych;
#4 linia - maksymalna osiągnięta głębokość rekursji;
#5 linia - czas trwania procesu obliczeniowego w sekundach (z dokładnością do milisekundy).
    with open(statistics_file, "w") as file_stat:               #zapis do pliku ze statystykami
        if solution_stat is None:
            file_stat.write("-1\n")
        else:
            length_statistics = 0

            while solution_stat.parent is not None:
                length_statistics += 1
                solution_stat = solution_stat.parent

            file_stat.write(str(length_statistics)+'\n')

        file_stat.write(str(visited_states)+'\n')
        file_stat.write(str(processed_states)+'\n')
        file_stat.write(str(max_recursion_depth)+'\n')
        file_stat.write("{:.3f}".format(duration))
        file_stat.close()