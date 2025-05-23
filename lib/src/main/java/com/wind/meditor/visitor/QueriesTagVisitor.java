package com.wind.meditor.visitor;

import com.wind.meditor.property.AttributeItem;
import com.wind.meditor.property.QueriesProperty;
import com.wind.meditor.utils.NodeValue;
import pxb.android.axml.NodeVisitor;

import java.util.ArrayList;
import java.util.List;

public class QueriesTagVisitor extends NodeVisitor {
    
    private List<QueriesProperty.Intent> intentsToAdd;
    private static final String INTENT_FLAG = "intent_flag";
    
    public QueriesTagVisitor(NodeVisitor nv, List<QueriesProperty.Intent> intentsToAdd) {
        super(nv);
        this.intentsToAdd = intentsToAdd;
    }
    
    @Override
    public NodeVisitor child(String ns, String name) {
        if (INTENT_FLAG.equals(ns)) {
            NodeVisitor nv = super.child(null, name);
            return new IntentTagVisitor(nv, null);
        }
        return super.child(ns, name);
    }
    
    private void addIntent(QueriesProperty.Intent intent) {
        NodeVisitor intentChild = super.child(null, NodeValue.Queries.Intent.TAG_NAME);
        IntentTagVisitor intentVisitor = new IntentTagVisitor(intentChild, intent);
        intentVisitor.end();
    }
    
    @Override
    public void end() {
        if (intentsToAdd != null) {
            for (QueriesProperty.Intent intent : intentsToAdd) {
                addIntent(intent);
            }
        }
        super.end();
    }
    
    private static class IntentTagVisitor extends NodeVisitor {
        private QueriesProperty.Intent intent;
        
        public IntentTagVisitor(NodeVisitor nv, QueriesProperty.Intent intent) {
            super(nv);
            this.intent = intent;
        }
        
        private void addAction(QueriesProperty.Intent.Action action) {
            NodeVisitor actionChild = super.child(NodeValue.MANIFEST_NAMESPACE, NodeValue.Queries.Intent.Action.TAG_NAME);
            ActionTagVisitor actionVisitor = new ActionTagVisitor(actionChild, action);
            actionVisitor.end();
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
        
        private static List<AttributeItem> createActionAttributes(QueriesProperty.Intent.Action action) {
            ArrayList<AttributeItem> list = new ArrayList<>();
            list.add(new AttributeItem(NodeValue.Queries.Intent.Action.NAME, action.getName()));
            return list;
        }
    }
}
