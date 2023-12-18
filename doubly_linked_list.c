#include <stdio.h>

// The stdlib library provides functions for memory allocation, conversion,
// random number generation, and other general-purpose utilities.
#include <stdlib.h>

// The string.h header file in C provides various functions for manipulating strings,
// such as string copying, concatenation, comparison, and searching. It also includes functions for converting strings to numeric values and vice versa.
#include <string.h>

// typedef int bool;
// #define true 1
// #define false 0
// bool isValid = true;

// typedef struct Node {
//     char* data;
//     struct Node* prev;
//     struct Node* next;
// } Node;

// similar to:
struct Node {
    char* data;
    struct Node* prev;
    struct Node* next;
};

typedef struct Node Node;

typedef struct DoublyLinkedList {
    Node* head;
    Node* tail;
} DoublyLinkedList;

DoublyLinkedList* createList() {
    DoublyLinkedList* lst = (DoublyLinkedList*)malloc(sizeof(DoublyLinkedList));
    lst->head = NULL;
    lst->tail = NULL;
    return lst;
}

void insertNodeToBack(DoublyLinkedList* lst, char* data) {
    Node* newNode = (Node*)malloc(sizeof(Node));
    newNode->data = (char*)malloc(strlen(data) + 1);
    strcpy(newNode->data, data);
    newNode->prev = NULL;
    newNode->next = NULL;

    if (lst->head == NULL) {
        lst->head = newNode;
        lst->tail = newNode;
    } else {
        newNode->prev = lst->tail;
        lst->tail->next = newNode;
        lst->tail = newNode;
    }
}


Node* findNode(DoublyLinkedList* lst, char* data) {
    Node* current = lst->head;
    while (current != NULL) {
        if (strcmp(current->data, data) == 0) {
            return current;
        }
        current = current->next;
    }
    return NULL;
}

Node* deleteNode(DoublyLinkedList* lst, char* data) {
    Node* nodeToDelete = findNode(lst, data);
    if (nodeToDelete == NULL) {
        return NULL;
    }

    if (nodeToDelete->prev == NULL) {
        lst->head = nodeToDelete->next;
    } else {
        nodeToDelete->prev->next = nodeToDelete->next;
    }

    if (nodeToDelete->next == NULL) {
        lst->tail = nodeToDelete->prev;
    } else {
        nodeToDelete->next->prev = nodeToDelete->prev;
    }

    free(nodeToDelete->data);
    free(nodeToDelete);

    return nodeToDelete;
}

void deleteList(DoublyLinkedList* lst) {
    Node* current = lst->head;
    while (current != NULL) {
        Node* temp = current;
        current = current->next;
        free(temp->data);
        free(temp);
    }
    free(lst);
}

void printList(DoublyLinkedList* lst) {
    Node* current = lst->head;
    while (current != NULL) {
        printf("%s ", current->data);
        current = current->next;
    }
    printf("\n");
}

int main() {
    DoublyLinkedList* lst = createList();

    insertNodeToBack(lst, "Hello");
    insertNodeToBack(lst, "World");
    insertNodeToBack(lst, "Copilot");

    printList(lst);

    Node* node = findNode(lst, "World");
    printf("Found node: %s\n", node->data);

    deleteNode(lst, "World");

    printList(lst);

    node = findNode(lst, "World");
    if (node == NULL) {
        printf("Node not found\n");
    }

    deleteList(lst);

    return 0;
}

// #include <stdio.h>
// #include <stdlib.h>
// #include <string.h>

// typedef struct Node {
//     char* data;
//     struct Node* prev;
//     struct Node* next;
// } Node;

// typedef struct {
//     Node* head;
//     Node* tail;
// } DoublyLinkedList;

// DoublyLinkedList* createList() {
//     DoublyLinkedList* list = (DoublyLinkedList*)malloc(sizeof(DoublyLinkedList));
//     list->head = NULL;
//     list->tail = NULL;
//     return list;
// }

// void insertNode(DoublyLinkedList* list, char* data) {
//     Node* newNode = (Node*)malloc(sizeof(Node));
//     newNode->data = (char*)malloc(strlen(data) + 1);
//     strcpy(newNode->data, data);
//     newNode->prev = NULL;
//     newNode->next = NULL;

//     if (list->head == NULL) {
//         list->head = newNode;
//         list->tail = newNode;
//     } else {
//         newNode->prev = list->tail;
//         list->tail->next = newNode;
//         list->tail = newNode;
//     }
// }

// void printList(DoublyLinkedList* list) {
//     Node* current = list->head;
//     while (current != NULL) {
//         printf("%s ", current->data);
//         current = current->next;
//     }
//     printf("\n");
// }

// void freeList(DoublyLinkedList* list) {
//     Node* current = list->head;
//     while (current != NULL) {
//         Node* temp = current;
//         current = current->next;
//         free(temp->data);
//         free(temp);
//     }
//     free(list);
// }

// int main() {
//     DoublyLinkedList* list = createList();

//     insertNode(list, "Hello");
//     insertNode(list, "World");
//     insertNode(list, "Copilot");

//     printList(list);

//     freeList(list);

//     return 0;
// }
