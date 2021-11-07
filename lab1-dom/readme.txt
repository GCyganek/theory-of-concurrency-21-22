Klasa Semaphore implementuje semafor binarny, IfSemaphore implementuje semafor binarny, ale z ifami zamiast while w P() i V().
Klasa CountingSemaphore implementuje semafor ogólny z wykorzystaniem semafora binarnego.
Użycie wszystkich trzech semaforów jest testowane w klasie Demo w metodzie main().

1. Różnica pomiędzy semaforem z if i while:
    Do implementacji semafora za pomocą metod wait i notify if zamiast while nie jest dobrym rozwiązaniem, gdyż powoduje
    on deadlocki i nieprawidłowe wartości końcowe (counter =/= 0). Dzieje się tak dlatego, że wg dokumentacji metoda wait()
    powinna być stosowana tylko w loopie, ponieważ możliwe są niepoprawne wybudzenia, czy przerwania w oczekiwaniu.
    Źródło - https://docs.oracle.com/javase/7/docs/api/java/lang/Object.html#wait()
    Ponadto nawet przy poprawnym wybudzeniu wymagane jest ponowne sprawdzenie stanu semafora, aby uniknąć sytuacji,
    w której przykładowo zostanie ponownie opuszczony już wcześniej opuszczony semafor.
    Praktyczny przykład:
    - Mamy opuszczony semafor oraz wątek X chcący wykonać opuszczenie semafora, czekający z instrukcją wait().
    - Wątek Y po opuszczeniu monitora podnosi semafor i używa metody notifyAll().
    - W tym samym momencie pojawia się nowy wątek Z, który chce opuścić semafor. Zauważa, że jest on podniesiony (po wykonaniu
    V() przez wątek Y), zatem wykonuje P() na semaforze
    - Wątek X po wybudzeniu zostaje uprzedzony przez wątek Z, jednak przez to, że nie sprawdza ponownie stanu semafora
    i tak wykonuje P()
    - W stanie końcowym wątki Y i Z mogą znaleźć się w sekcji krytycznej i operować na tych samych zasobach, co może
    doprowadzić do błędnych wyników.

2. Semafor licznikowy
    Semafor binarny to szczególny przypadek semafora licznikowego. Oba przechowują wartości od 0 do N (dla semafora
    binarnego N = 1), gdzie proces chcący opuścić semafor przy wartości 0 musi poczekać na jej podniesienie. Semafor
    licznikowy przyjumjący N = 1 to semafor binarny i pozwala na wejście do sekcji krytycznej tylko jednemu procesowi
    w danym momencie.