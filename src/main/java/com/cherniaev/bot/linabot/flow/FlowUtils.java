package com.cherniaev.bot.linabot.flow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum FlowUtils {
    REQUEST_TO_START_POOL_ANSWERS {
        @Override
        public List<String> getAnswers() {
            List<String> answers = new ArrayList<>();
            answers.add("Да");
            return answers;
        }
    },
    TIMES_ANSWERS {
        @Override
        public List<String> getAnswers() {
            List<String> answers = new ArrayList<>();
            answers.add("0 - 1");
            answers.add("1 - 3");
            answers.add("3 - 5");
            return answers;
        }
    },
    EXPERIENCE_ANSWERS {
        @Override
        public List<String> getAnswers() {
            List<String> answers = new ArrayList<>();
            answers.add("Меньше 6 месяцев");
            answers.add("6 - 12 месяцев");
            answers.add("Больше года");
            return answers;
        }
    },
    CALORIES_ANSWERS {
        @Override
        public List<String> getAnswers() {
            List<String> answers = new ArrayList<>();
            answers.add("Да");
            answers.add("Нет");
            return answers;
        }
    },
    MEDITATION_ANSWERS {
        @Override
        public List<String> getAnswers() {
            List<String> answers = new ArrayList<>();
            answers.add("Да");
            answers.add("Нет");
            return answers;
        }
    };

    public List<String> getAnswers() {
        return Collections.emptyList();
    }
}
