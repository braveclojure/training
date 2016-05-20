// ========================================
// Recursion instead of loop/for
// ========================================

// In JS and other imperative languages you often do something like
// this:
var dailySteps = [2000 5002 6340 10001];
var l = dailySteps.length;
var totalSteps = 0;

// sum
for (var i = 0; i < l; i++) {
  totalSteps += dailySteps[i];
}

// or this:
var stepDays = [
  {"day": 1, "steps": 2000},
  {"day": 2, "steps": 5002},
  {"day": 3, "steps": 6340},
  {"day": 4, "steps": 10001},
];
var l = stepDays.length;
var goodDays = []

// filter
for (var i = 0; i < l; i++) {
  if stepDays[i].steps > 6000 {
    goodDays.push(stepDays[i]);
  }
}
