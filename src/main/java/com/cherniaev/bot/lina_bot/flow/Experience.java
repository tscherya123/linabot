package com.cherniaev.bot.lina_bot.flow;

public enum Experience {
    SIX_MONTH {
        public String toString() {
            return "< 6 месяцев";
        }
    },
    YEAR {
        public String toString() {
            return "6 - 12 месяцев";
        }
    },
    MORE {
        public String toString() {
            return "> 12 месяцев";
        }
    }
}
