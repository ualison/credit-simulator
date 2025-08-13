const fs = require('fs');

function randomBirthDate() {
  const start = new Date(1940, 0, 1).getTime();
  const end = new Date(2000, 11, 31).getTime();
  const t = Math.floor(Math.random() * (end - start + 1)) + start;
  const d = new Date(t);
  return d.toISOString().slice(0, 10); // YYYY-MM-DD
}

function randomLoanAmount(min = 1000, max = 100000) {
  return Number((Math.random() * (max - min) + min).toFixed(2));
}

function randomPaymentTerm() {
  return Math.floor(Math.random() * (360 - 6 + 1)) + 6;
}

function randomInterestType() {
  return Math.random() < 0.5 ? 'FIXED' : 'VARIABLE';
}

function randomCurrency() {
  const currencies = ['BRL', 'USD', 'EUR'];
  return currencies[Math.floor(Math.random() * currencies.length)];
}

const COUNT = 6000;
const simulations = [];

for (let i = 0; i < COUNT; i++) {
  simulations.push({
    loanAmount: randomLoanAmount(),
    birthDate: randomBirthDate(),
    paymentTermMonths: randomPaymentTerm(),
    interestRateType: randomInterestType(),
    currency: randomCurrency()
  });
}

const dataset = { simulations };

fs.writeFileSync('dataset.json', JSON.stringify(dataset, null, 2), 'utf8');
console.log(`dataset.json gerado com ${COUNT} registros.`);