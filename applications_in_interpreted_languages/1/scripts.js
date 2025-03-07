// Włączenie trybu strict, który wymusza np. że
// Zmienne muszą być zadeklarowane przed użyciem, zapobiega to przypadkowym błędom.
"use strict";

let todoList = [];
let YOUR_API_KEY = "$2a$10$7yml0mkRnskhOmXJr0iFa.foC5QVFbItkUiyZzBim2qHVJe83dK3W";
let BIN_ID = "66fc1acead19ca34f8b10866";
let GROQ_API_KEY = "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";

// Tworzenie żądania HTTP do pobrania danych z JSONbin.
let req = new XMLHttpRequest();

// Funkcja uruchamiana, gdy zmienia się stan żądania (readyState).
req.onreadystatechange = () => {
    // Sprawdzenie, czy żądanie zostało zakończone (DONE).
    if (req.readyState == XMLHttpRequest.DONE) {
        // Aktualizacja listy zadań na podstawie danych z odpowiedzi.
        todoList = JSON.parse(req.responseText).record;
    }
};

// Żądanie GET do pobrania najnowszych danych z JSONbin.
req.open("GET", `https://api.jsonbin.io/v3/b/${BIN_ID}/latest`, true);

// Podanie klucza API
req.setRequestHeader("X-Master-Key", YOUR_API_KEY);

// Wypisanie błędu w przypadku problemu z nawiązaniem żądania
req.onerror = function() {
    console.error("An error occurred during the request");
};

// Wysłanie żądania.
req.send();

// Funkcja aktualizująca dane w JSONbin (zapis nowej listy zadań).
let updateJSONbin = function() {
    let req = new XMLHttpRequest();

    // Funkcja uruchamiana przy zmianie stanu żądania (readyState).
    req.onreadystatechange = () => {
        if (req.readyState == XMLHttpRequest.DONE) {
            // Aktualizacja listy zadań na podstawie odpowiedzi.
            todoList = JSON.parse(req.responseText).record;
        }
    };

    // Wysłanie PUT do aktualizacji danych w JSONbin.
    req.open("PUT", `https://api.jsonbin.io/v3/b/${BIN_ID}`, true);

    // Ustawienie nagłówków: typ treści i klucz API.
    req.setRequestHeader("Content-Type", "application/json");
    req.setRequestHeader("X-Master-Key", YOUR_API_KEY);

    // Wysłanie zaktualizowanej listy zadań w formacie JSON.
    req.send(JSON.stringify(todoList));
};

// Funkcja aktualizująca widok listy zadań (przefiltrowana lista na podstawie kryteriów użytkownika).
let updateTodoList = function() {
    let todoTableBody = document.getElementById("todoListView"); // Pobranie tabeli zadań.

    // Usunięcie wszystkich istniejących wierszy tabeli.
    while (todoTableBody.firstChild) {
        todoTableBody.removeChild(todoTableBody.firstChild);
    }

    // Pobranie tekstu wyszukiwania oraz konwersja na małe litery.
    let filterInput = document.getElementById("inputSearch").value.toLowerCase();

    // Pobranie wartości zakresu dat (od i do).
    let filterStartDate = document.getElementById("filterStartDate").value;
    let filterEndDate = document.getElementById("filterEndDate").value;

    // Konwersja wartości dat na obiekty Date (lub null, jeśli nie ustawiono daty).
    let startDate = filterStartDate ? new Date(filterStartDate) : null;
    let endDate = filterEndDate ? new Date(filterEndDate) : null;

    // Filtrowanie i wyświetlanie zadań na podstawie wyszukiwania i zakresu dat.
    todoList.filter(todo => {
        let todoDueDate = todo.dueDate ? new Date(todo.dueDate) : null; // Konwersja daty zadania na obiekt Date.

        // Sprawdzenie, czy zadanie pasuje do kryteriów wyszukiwania (tytuł lub opis).
        let matchesSearch = filterInput === "" ||
            todo.title.toLowerCase().includes(filterInput) ||
            todo.description.toLowerCase().includes(filterInput);

        // Sprawdzenie, czy zadanie mieści się w zakresie dat.
        let matchesDateRange = true;
        if (startDate && endDate && todoDueDate) {
            matchesDateRange = todoDueDate >= startDate && todoDueDate <= endDate;
        } else if (startDate && todoDueDate) {
            matchesDateRange = todoDueDate >= startDate;
        } else if (endDate && todoDueDate) {
            matchesDateRange = todoDueDate <= endDate;
        }

        return matchesSearch && matchesDateRange;
    }).forEach((todo, i) => {
        // Tworzenie nowego wiersza tabeli dla każdego pasującego zadania.
        let row = document.createElement("tr");

        // Tytuł zadania
        let titleCell = document.createElement("td");
        titleCell.textContent = todo.title;
        row.appendChild(titleCell);

        // Opis zadania
        let descriptionCell = document.createElement("td");
        descriptionCell.textContent = todo.description;
        row.appendChild(descriptionCell);

        // Miejsce.
        let placeCell = document.createElement("td");
        placeCell.textContent = todo.place;
        row.appendChild(placeCell);

        // Data
        let dueDateCell = document.createElement("td");
        dueDateCell.textContent = todo.dueDate ? new Date(todo.dueDate).toLocaleDateString() : '';
        row.appendChild(dueDateCell);

        // Kategoria
        let categoryCell = document.createElement("td");
        categoryCell.textContent = todo.category || '';
        row.appendChild(categoryCell);

        // Przycisk usunięcia
        let actionsCell = document.createElement("td");
        let deleteButton = document.createElement("input");
        deleteButton.type = "button";
        deleteButton.value = "Delete";
        deleteButton.className = "btn btn-danger";

        // Dodanie funkcji do usunięcia zadania po kliknięciu przycisku.
        deleteButton.addEventListener("click", function() {
            deleteTodo(i);
        });
        actionsCell.appendChild(deleteButton);
        row.appendChild(actionsCell);

        // Dodanie nowego wiersza do tabeli.
        todoTableBody.appendChild(row);
    });
};

// Automatyczna aktualizacja listy zadań co sekundę.
setInterval(updateTodoList, 1000);

// Funkcja do usuwania zadania z listy.
let deleteTodo = function(index) {
    todoList.splice(index,1); // Usunięcie zadania z listy.
    updateJSONbin(); // Aktualizacja danych do JSONBin
}

async function categorizeTask(title, description) {
    const prompt = `Categorize the following task as either "uczelnia" or "prywatne". Respond with only one word, either "uczelnia" or "prywatne":
    Title: ${title}
    Description: ${description}`;

    try {
        // Wysłanie żądania do API z tytułem i opisem zadania w celu określenia kategorii.
        const response = await fetch('https://api.groq.com/openai/v1/chat/completions', {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${GROQ_API_KEY}`, // Autoryzacja za pomocą klucza API.
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                model: "llama3-8b-8192", // Model używany przez API.
                messages: [{ role: "user", content: prompt }],
                max_tokens: 10,
                temperature: 0.2
            })
        });

        // Przetwarzanie odpowiedzi z API i otrzymanie kategorii.
        const data = await response.json();
        const category = data.choices[0].message.content.trim().toLowerCase();

        // Sprawdzenie, czy zwrócona kategoria jest poprawna.
        if (category === "uczelnia" || category === "prywatne") {
            return category;
        } else {
            console.warn("Invalid category received: ", category);
            return "inne"; // Domyślna kategoria w przypadku błędu.
        }
    } catch (error) {
        console.error("Error categorizing task: ", error);
        return "inne"; // Domyślna kategoria w przypadku błędu.
    }
}

// Funkcja dodająca nowe zadanie.
let addTodo = async function() {
    // Pobranie elementów formularza.
    let inputTitle = document.getElementById("inputTitle");
    let inputDescription = document.getElementById("inputDescription");
    let inputPlace = document.getElementById("inputPlace");
    let inputDate = document.getElementById("inputDate");

    // Pobranie wartości z formularza.
    let newTitle = inputTitle.value;
    let newDescription = inputDescription.value;
    let newPlace = inputPlace.value;
    let newDate = new Date(inputDate.value);

    let category = await categorizeTask(newTitle, newDescription);

    // Tworzenie nowego obiektu zadania.
    let newTodo = {
        title: newTitle,
        description: newDescription,
        place: newPlace,
        dueDate: newDate,
        category: category
    };

    // Dodanie zadania do listy.
    todoList.push(newTodo);

    // Aktualizacja JSONbin.
    updateJSONbin();

    // Wyczyszczenie formularza po dodaniu zadania.
    inputTitle.value = "";
    inputDescription.value = "";
    inputPlace.value = "";
    inputDate.value = "";

    // Zaktualizowanie listy zadań.
    updateTodoList();
}
