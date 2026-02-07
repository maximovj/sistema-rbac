// src/common/utils/loggerUtils.js
const ENABLE_LOGS = import.meta.env.VITE_ENABLE_LOGS === 'true';
const APP_PREFIX = '[RHHUB-APP]';

const now = () =>
  new Date().toLocaleString('es-MX', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
  });

const format = (level, args) => [
  `${APP_PREFIX}  ${now()} ${level}`,
  ...args,
];

export const log = (...args) => {
  if (ENABLE_LOGS) console.log(...format('[📍LOG]', args));
};

export const info = (...args) => {
  if (ENABLE_LOGS) console.info(...format('[ℹ️INFO]', args));
};

export const warn = (...args) => {
  if (ENABLE_LOGS) console.warn(...format('[⚠️WARN]', args));
};

export const error = (...args) => {
  if (ENABLE_LOGS) console.error(...format('[🔴ERROR]', args));
};

export const scopedLogger = (scope) => ({
  log: (...args) => log(`[${scope} ${args[0]? ('::'+args[0]) : ''}]`, args.slice(1)),
  info: (...args) => info(`[${scope} ${args[0]? ('::'+args[0]) : ''}]`, args.slice(1)),
  warn: (...args) => warn(`[${scope} ${args[0]? ('::'+args[0]) : ''}]`, args.slice(1)),
  error: (...args) => error(`[${scope} ${args[0]? ('::'+args[0]) : ''}]`, args.slice(1)),
});