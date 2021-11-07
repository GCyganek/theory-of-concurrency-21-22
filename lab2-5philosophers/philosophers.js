// Teoria Współbieżności, implementacja problemu 5 filozofów w node.js
// Opis problemu: http://en.wikipedia.org/wiki/Dining_philosophers_problem
//   https://pl.wikipedia.org/wiki/Problem_ucztuj%C4%85cych_filozof%C3%B3w
// 1. Dokończ implementację funkcji podnoszenia widelca (Fork.acquire).
// 2. Zaimplementuj "naiwny" algorytm (każdy filozof podnosi najpierw lewy, potem
//    prawy widelec, itd.).
// 3. Zaimplementuj rozwiązanie asymetryczne: filozofowie z nieparzystym numerem
//    najpierw podnoszą widelec lewy, z parzystym -- prawy. 
// 4. Zaimplementuj rozwiązanie z kelnerem (według polskiej wersji strony)
// 5. Zaimplementuj rozwiążanie z jednoczesnym podnoszeniem widelców:
//    filozof albo podnosi jednocześnie oba widelce, albo żadnego.
// 6. Uruchom eksperymenty dla różnej liczby filozofów i dla każdego wariantu
//    implementacji zmierz średni czas oczekiwania każdego filozofa na dostęp 
//    do widelców. Wyniki przedstaw na wykresach.

// zrobic sprawko i wykresy z wynikow

var numberOfPhilosophers;
var cbFinish;

function getRandomFromRange(min, max) {
    return Math.floor(Math.random() * (max - min) + min);
}

function loopBEB(fork, waitTime, cb) {
    setTimeout(() => {
        if (fork.state == 0) {
            fork.state = 1;
            cb();
        } else {
            loopBEB(fork, waitTime * 2, cb);
        }
    }, waitTime);
}

function loopBEB2(fork1, fork2, waitTime, cb) {
    setTimeout(() => {
        if (fork1.state == 0 && fork2.state == 0) {
            fork1.state = 1;
            fork2.state = 1;
            cb();
        } else {
            loopBEB2(fork1, fork2, waitTime * 2, cb);
        }
    }, waitTime);
}

function acquireTwoForks(fork1, fork2, cb) {
    loopBEB2(fork1, fork2, 1, cb);
}



class Fork {
    constructor() {
        this.state = 0;
        return this;
    }
    acquire(cb) {
        // zaimplementuj funkcję acquire, tak by korzystala z algorytmu BEB
        // (http://pl.wikipedia.org/wiki/Binary_Exponential_Backoff), tzn:
        // 1. przed pierwszą próbą podniesienia widelca Filozof odczekuje 1ms
        // 2. gdy próba jest nieudana, zwiększa czas oczekiwania dwukrotnie
        //    i ponawia próbę, itd.
        loopBEB(this, 1, cb);
    }
    release() {
        this.state = 0;
    }
}



class Philosopher {
    constructor(id, forks) {
        this.id = id;
        this.forks = forks;
        this.f1 = id % forks.length;
        this.f2 = (id + 1) % forks.length;
        this.totalWaitTime = 0;
        return this;
    }

    averageWaitTime(count) {
        return Math.floor(this.totalWaitTime / count);
    }

    startNaive(count) {
        var forks = this.forks,
            f1 = this.f1,
            f2 = this.f2,
            id = this.id;
        // zaimplementuj rozwiązanie naiwne
        // każdy filozof powinien 'count' razy wykonywać cykl
        // podnoszenia widelców -- jedzenia -- zwalniania widelców

        this.#loop(count, forks, f1, f2, id);
    }

    startAsym(count) {
        var forks = this.forks,
            id = this.id,
            f1 = id % 2 == 0 ? this.f2 : this.f1,
            f2 = id % 2 == 0 ? this.f1 : this.f2;        
        // zaimplementuj rozwiązanie asymetryczne
        // każdy filozof powinien 'count' razy wykonywać cykl
        // podnoszenia widelców -- jedzenia -- zwalniania widelców

        this.#loop(count, forks, f1, f2, id);
    }

    // naive i asym działają tak samo, zmieniają się tylko f1 i f2 w zależności od id
    #loop(count, forks, f1, f2, id) {
        if (count > 0) {
            console.log(`Philosopher ${id} is thinking...`);
            setTimeout(() => {
                let startWaiting = new Date().getTime();
                forks[f1].acquire(() => {
                    forks[f2].acquire(() => {
                        let endWaiting = new Date().getTime();
                        this.totalWaitTime += endWaiting - startWaiting;
                        console.log(`Philosopher ${id} acquired forks`);
                        setTimeout(() => {
                            console.log(`Philosopher ${id} finished eating, ${count - 1} dishes left` );
                            forks[f1].release();
                            forks[f2].release();
                            this.#loop(count - 1, forks, f1, f2, id);
                        }, 100);
                    });
                });
            }, getRandomFromRange(20, 81));
        } else {
            numberOfPhilosophers--;
            if (numberOfPhilosophers == 0) {
                cbFinish();
            }
        }
    }

    startConductor(count, conductor) {
        var forks = this.forks,
            id = this.id,
            f1 = this.f1,
            f2 = this.f2,
            philosopher = this;
        // zaimplementuj rozwiązanie z kelnerem
        // każdy filozof powinien 'count' razy wykonywać cykl
        // podnoszenia widelców -- jedzenia -- zwalniania widelców

        var loopConductor = (count) => {
            if (count > 0) {
                console.log(`Philosopher ${id} is thinking...`);
                setTimeout(() => {
                        console.log(`Philosopher ${id} is asking to be served`);
                        let startWaiting = new Date().getTime();
                        conductor.serve(philosopher, () => {
                            forks[f1].acquire(() => {
                                console.log(`Philosopher ${id} acquired his left fork`);
                                forks[f2].acquire(() => {
                                    let endWaiting = new Date().getTime();
                                    philosopher.totalWaitTime += endWaiting - startWaiting;
                                    console.log(`Philosopher ${id} acquired his right fork`);
                                    setTimeout(() => {
                                        console.log(`Philosopher ${id} finished eating, ${count - 1} dishes left` );
                                        forks[f1].release();
                                        forks[f2].release();
                                        conductor.freeSeat(philosopher, () => {
                                            loopConductor(count - 1);
                                        });
                                    }, 100);
                                });
                            });
                        });
                }, getRandomFromRange(20, 81));
            } else {
                numberOfPhilosophers--;
                if (numberOfPhilosophers == 0) {
                    cbFinish();
                }
            }
        }

        loopConductor(count);
    }

    // TODO: wersja z jednoczesnym podnoszeniem widelców
    // Algorytm BEB powinien obejmować podnoszenie obu widelców, 
    // a nie każdego z osobna

    startTwoForks(count) {
        var forks = this.forks,
            f1 = this.f1,
            f2 = this.f2,
            id = this.id,
            philosopher = this;
        
        var loopTwoForks = (count) => {
            if (count > 0) {
                console.log(`Philosopher ${id} is thinking...`);
                setTimeout(() => {
                    let startWaiting = new Date().getTime();
                    acquireTwoForks(forks[f1], forks[f2], () => {
                        let endWaiting = new Date().getTime();
                        philosopher.totalWaitTime += endWaiting - startWaiting;
                        console.log(`Philosopher ${id} acquired forks`);
                        setTimeout(() => {
                            console.log(`Philosopher ${id} finished eating, ${count - 1} dishes left` );
                            forks[f1].release();
                            forks[f2].release();
                            loopTwoForks(count - 1);
                        }, 100); 
                    });
                }, getRandomFromRange(20, 81));
            } else {
                numberOfPhilosophers--;
                if (numberOfPhilosophers == 0) {
                    cbFinish();
                }
            }
        } 

        loopTwoForks(count);
    }
}

class Conductor {
    constructor(forks) {
        this.forks = forks;
        this.queue = [];
        this.philosophersAtTheTable = [];
        return this;
    }

    serve(philosopher, cb) {
        var id = philosopher.id,
            f1 = philosopher.f1,
            f2 = philosopher.f2;
        
            if (this.checkSeats(f1, f2)) {
                console.log(`Philosopher ${id} can take a sit and eat`);
                this.philosophersAtTheTable.push(philosopher);
                setTimeout(cb)
            } else {
                this.queue.push([philosopher, cb]);
            }
    }

    checkSeats(f1, f2) {
        let canTakeASit = true;
        // checks if any philosopher that has been allowed to sit at the table will or has a fork that now a new philosopher is asking for
        this.philosophersAtTheTable.forEach(philosopher => {
            if (philosopher.f1 == f1 || philosopher.f1 == f2 ||
                philosopher.f2 == f1 || philosopher.f2 == f2) {
                canTakeASit = false;
            }
        });
        return canTakeASit;
    }

    freeSeat(philosopher, cb) {
        this.philosophersAtTheTable = this.philosophersAtTheTable.filter(philosopherAtTheTable => philosopherAtTheTable.id != philosopher.id);

        // if now any philosopher from queue can sit and take both forks, serve him
        for (var i = 0; i < this.queue.length; i++) {
            var philosopherFromQueue = this.queue[i][0],
                cbFromQueue = this.queue[i][1];
            if (this.checkSeats(philosopherFromQueue.f1, philosopherFromQueue.f2)) {
                this.queue.splice(i, 1);
                console.log(`Philosopher ${philosopherFromQueue.id} can take a sit and eat`);
                this.philosophersAtTheTable.push(philosopherFromQueue)
                setTimeout(cbFromQueue);
            }
        }
        setTimeout(cb);
    }

}

const { exit } = require('process');
const readline = require('readline').createInterface({
    input: process.stdin,
    output: process.stdout
});

readline.question('Enter number of philosophers: ', (inputNum1) => {
    numberOfPhilosophers = parseInt(inputNum1);
    
    readline.question('Enter number of meals they are going to eat: ', (inputNum2) => {
        var numberOfMeals = parseInt(inputNum2),
            forks = [],
            philosophers = [];

        for (var i = 0; i < numberOfMeals; i++) {
            forks.push(new Fork());
        }
        
        for (var i = 0; i < numberOfPhilosophers; i++) {
            philosophers.push(new Philosopher(i, forks));
        }
            
        
        readline.question('Enter mode (0 - naive, 1 - asymetric, 2 - conductor, 3 - two forks at once): ', (inputNum3) => {
            var mode = parseInt(inputNum3);
            readline.close();

            cbFinish = () => {
                console.log('\n\nAverage times:')
                philosophers.forEach(philosopher => {
                    console.log(`\tPhilosopher ${philosopher.id}: average time waiting ${philosopher.averageWaitTime(numberOfMeals)}`);
                });
            }

            switch (mode) {
                case 0:
                    for (var i = 0; i < numberOfPhilosophers; i++) {
                        philosophers[i].startNaive(numberOfMeals);
                    }
                    break;
                case 1:
                    for (var i = 0; i < numberOfPhilosophers; i++) {
                        philosophers[i].startAsym(numberOfMeals);
                    }
                    break;
                case 2:
                    var conductor = new Conductor(forks);
                    for (var i = 0; i < numberOfPhilosophers; i++) {
                        philosophers[i].startConductor(numberOfMeals, conductor);
                    }
                    break;
                case 3:
                    for (var i = 0; i < numberOfPhilosophers; i++) {
                        philosophers[i].startTwoForks(numberOfMeals);
                    }
                    break;
                default:
                    console.log("Wrong input");
                    exit(1);        
            }
        })
    })
})