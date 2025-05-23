package com.wind.meditor.property;

import java.util.ArrayList;
import java.util.List;

public class QueriesProperty {
    
    public static class Intent {
        private List<Action> actions = new ArrayList<>();
        
        public static class Action {
            private String name;
            
            public Action(String name) {
                this.name = name;
            }
            
            public String getName() {
                return name;
            }
        }
        
        public Intent addAction(String actionName) {
            actions.add(new Action(actionName));
            return this;
        }
        
        public List<Action> getActions() {
            return actions;
        }
    }
    
    private List<Intent> intents = new ArrayList<>();
    
    public QueriesProperty addIntent(Intent intent) {
        intents.add(intent);
        return this;
    }
    
    public List<Intent> getIntents() {
        return intents;
    }
}
