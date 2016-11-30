var assert = require('assert');
var date = require('../public/js/date');

describe('test date', function() {
    it('should return correct number of week', function() {
        assert.equal(1, date.getWeek(new Date('January 1, 2016')));
    });

    it('should return even week', function() {
        assert.equal('even', date.oddOrEven(33));
    });

    it('should return odd week', function() {
        assert.equal('odd', date.oddOrEven(34));
    });
});