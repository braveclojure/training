# blackjack

Create a command-line blackjack game.

http://www.bicyclecards.com/how-to-play/blackjack/ has the rules to
blackjack, in case you find that helpful. Ignore the parts about
splitting pairs, doubling down, and insurance.

## Gameplay

Do `lein run` to try out a working version of the game. Below I
describe the gameplay in detail.

When the application starts, the player has $1,000. The player is
presented with a prompt to place a bet, like this:

```
You have  1000  money
Please place a bet by entering a number:
```

After the player places a bet, the game should deal to him and to the
"dealer".

1. Deal a card to the player
2. Deal a card to the dealer
3. Deal a card to the player
4. Deal a card to the dealer

You don't have to give any progress indicators for dealing, just show
what was dealt, like this:

```
-----
Your hand: ♣5, ♥3 (8)
Dealer hand: ♥10
Bet: 100
-----

(h)it or (s)tand?
```

Notice a couple things:

* Your hand's total is displayed
* One of the dealer's cards is hidden, like in real blackjack
* The dealer's total is not displayed

At this point you can `(h)it` or `(s)tand`. `(s)tand` means pass play
to the dealer (detailed below).

If the user selects `h` for hit, deal her a card. Check that the user
hasn't busted (gone over 21).

Note that Aces can count as either a 1 or 11. When calculating the
point value for a hand, take this into account:

* At first, count Aces as 11's
* If the player's hand is a bust, change one Ace from 11 to 1
* If the player's hand is still a bust, keep changing existing Aces
  from 11 to 1

If the player busts, the round is over and the player loses the money.

If the player doesn't bust, play passes to the dealer:

* If the dealer's total is 17 or more, he must stand
* If the total is 16 or under, dealer must take a card
* If the dealer has an ace and counting it as an 11 would bring his
  total to 17 or above, he must count the ace as 11 and stand

Now the bet is settled. If the player has a higher score and didn't
bust, or if the dealer busts, the player wins back 2x his bet. So if
he bet $100, he gets back the original $100 and an additonal $100. If
there's a tie, the player gets back his money. If the dealer wins, the
player loses his money.

## Naturals

If the player is dealt 21 - an ace and a ten card (including 10, jack,
queen, king) - then he has gotten a natural. If the dealer does not
have a natural as well, then the player gets 2.5x the bet; if he bet
$100, he gets that $100 back plus $150.

If the player and dealer both have naturals, it's a tie and the player
gets her money back.

If the dealer gets a natural and the player doesn't, the dealer wins.

## Hints

Try to break this down into smaller parts as much as possible, and
work on the smaller parts one at a time.

If it's too much, try going through the provided solution and
re-writing it in your own project. This is a great way to learn.
