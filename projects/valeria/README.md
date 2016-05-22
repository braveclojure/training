# Valeria

Part of the strategy of Valeria is building an effective
"tableau". This application will help you explore different tableau
possibilities.

Create a command-line Clojure application that does the following:

* Lists all Citizen cards
* Lets you buy a card
* Lets you return a card
* Lets you view a card

Remember that `lein run` will run an application.

## Listing all Citizen cards

When the application first loads, the user should see a numbered list
of Citizen cards. Each list entry should include:

* An index
* The Citizen's name
* The base cost
* The number you've bought, in parentheses

Like this:

```
0. Cleric 3         (0)
1. Merchant 2       (1)
2. Mercenary 3      (0)
3. Archer 4         (0)
4. Peasant 2        (0)
5. Knight 2         (0)
6. Rogue 2          (0)
7. Champion 2       (0)
8. Paladin 2        (0)
9. Butcher 1        (0)
Total cost:  2
(b)uy, (r)eturn, or (v)iew a card, or (reset). Ex: "b 0" to buy cleric
```

As you can see, this dashboard also includes the total cost every card
you've bought so far, and a list of commands.

## Buying cards

The command line application should let you type `b 0` to, for
example, buy a cleric. This should increment the count of clerics
you've bought. When you buy another cleric, its cost should go up,
reflecting the game's rules.

For example, the cleric's base cost is 3. You have to pay an
additional gold for every cleric you own, so if you own two clerics
it will cost 5 gold to buy a third.

## Returning cards

Let the user type `r 0` to return a cleric. Don't let the owned count
get into negative numbers; you should never own -1 clerics.

## View a card

Type `v 0` to view all the details for the cleric. Here are the
cleric's details:

```
========
Name:         Cleric
Base cost:    3
Hits on:      1
You payout:   3 magic
Other payout: 1 magic
Odds:         11/36
Role:         holy
========
```

Some cards, like the merchant, have more complex payout
options. Here's how the merchant should display:

```
========
Name:         Merchant
Base cost:    2
Hits on:      2
You payout:   2 gold || 2 magic
Other payout: 1 gold
Odds:         1/3
Role:         worker
========
```

Its payout is 2 gold OR 2 magic, and that's shown as `2 gold || 2
magic`.


## Extra credit

When you display the dashboard, display the per-turn value of all your
cards based on all cards you own and their probabilities of hitting.

(note: I don't actually know how to do this, but it sounds fun!)
