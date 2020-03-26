const prod = process.env.NODE_ENV === 'production';  // <1>

console.log(`Loading ${process.env.NODE_ENV} config...`);

export const SERVER_URL =  'http://localhost:11000';