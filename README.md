# Perceptron
Program umożliwia trening perceptronu rozpoznającego 2 klasy.

Użytkownik podaje jako argumenty:
ścieżka do pliku z danymi treningowymi
ścieżka do pliku z danymi testowymi
stała uczenia
liczba epok - jedna epoka to jedna iteracja przez całe dane treningowe

Wagi i próg perceptronu są inicjowane jako małe liczby, bliskie 0. Perceptron jest trenowany na danych treningowych przy użyciu reguły delta przez zadaną liczbę epok.
Program wypisuje dokładność na zbiorze testowym po każdej epoce.

Dane treningowe mogą być mieszane po każdej epoce, aby uzyskać lepsze wyniki.

Po uczeniu i testowaniu program w pętli pyta:
wprowadź nowe obserwacje z wiersza poleceń i przewiduj klasę
zakończ program

Program jest uniwersalny, powinien działać dla dowolnej liczby atrybutów liczbowych i jednego atrybutu decyzyjnego, który może przyjmować dokładnie 2 wartości nominalne.

