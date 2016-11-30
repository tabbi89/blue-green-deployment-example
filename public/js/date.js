function getWeek(date) {
    var onejan = new Date(date.getFullYear(), 0, 1);
    return Math.ceil((((date - onejan) / 86400000) + onejan.getDay() + 1) / 7);
}

function oddOrEven(x) {
    return (x % 2 == 0) ? "odd" : "even";
}

// :)
if (typeof exports !== 'undefined') {
    exports.getWeek =  getWeek;
    exports.oddOrEven =  oddOrEven;
}