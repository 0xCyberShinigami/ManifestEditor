package com.wind.meditor.visitor;

import com.wind.meditor.property.QueriesProperty;
import com.wind.meditor.utils.NodeValue;
import pxb.android.axml.NodeVisitor;

public class QueriesTagVisitor extends NodeVisitor {
    
    private QueriesProperty.Intent currentIntent;
    private static final String INTENT_FLAG = "intent_flag";
    private static final String ACTION_FLAG = "action_flag";
    
    public QueriesTagVisitor(NodeVisitor nv, QueriesProperty.Intent intent) {
        super(nv);
        this.currentIntent = intent;
    }
    
    @Override
    public NodeVisitor child(String ns, String name) {
        if (INTENT_FLAG.equals(ns)) {
            NodeVisitor nv = super.child(null, name);
            if (currentIntent != null) {
                return new IntentTagVisitor(nv, currentIntent);
            }
        }
        return super.child(ns, name);
    }
    
    private void addIntent(QueriesProperty.Intent intent) {
        currentIntent = intent;
        child(INTENT_FLAG, NodeValue.Queries.Intent.TAG_NAME);
        currentIntent = null;
    }
    
    @Override
    public void end() {
        if (currentIntent != null) {
            addIntent(currentIntent);
        }
        super.end();
    }
    
    private static class IntentTagVisitor extends NodeVisitor {
        private QueriesProperty.Intent intent;
        private QueriesProperty.Intent.Action currentAction;
        
        public IntentTagVisitor(NodeVisitor nv, QueriesProperty.Intent intent) {
            super(nv);
            this.intent = intent;
        }
        
        @Override
        public NodeVisitor child(String ns, String name) {
            if (ACTION_FLAG.equals(ns)) {
                NodeVisitor nv = super.child(null, name);
                if (currentAction != null) {
                    return new ActionTagVisitor(nv, currentAction);
                }
            }
            return super.child(ns, name);
        }
        
        private void addAction(QueriesProperty.Intent.Action action) {
            currentAction = action;
            child(ACTION_FLAG, NodeValue.Queries.Intent.Action.TAG_NAME);
            currentAction = null;
        }
        
        @Override
        public void end() {
            if (intent != null) {
                for (QueriesProperty.Intent.Action action : intent.getActions()) {
                    addAction(action);
                }
            }
            super.end();
        }
    }
    
    private static class ActionTagVisitor extends ModifyAttributeVisitor {
        public ActionTagVisitor(NodeVisitor nv, QueriesProperty.Intent.Action action) {
            super(nv, createActionAttributes(action), true);
        }
        
        private static java.util.List<com.wind.meditor.property.AttributeItem> createActionAttributes(QueriesProperty.Intent.Action action) {
            java.util.ArrayList<com.wind.meditor.property.AttributeItem> list = new java.util.ArrayList<>();
            list.add(new com.wind.meditor.property.AttributeItem(NodeValue.Queries.Intent.Action.NAME, action.getName()));
            return list;
        }
    }
}
