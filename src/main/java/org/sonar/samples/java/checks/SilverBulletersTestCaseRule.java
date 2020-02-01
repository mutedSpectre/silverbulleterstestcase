package org.sonar.samples.java.checks;

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.*;

import java.util.Arrays;
import java.util.List;

@Rule(
        key = "SilverBulletersTestCaseRule",
        name = "Stop use sun.misc.Unsafe.defineClass!!!!",
        description = "sun.misc.Unsafe.defineClass deprecated in Java 9+. USE java.lang.invoke.MethodHandles.Lookup.defineClass in Java 9+",
        priority = Priority.MAJOR,
        tags = {"code-smell"})
public class SilverBulletersTestCaseRule extends IssuableSubscriptionVisitor {

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return Arrays.asList(Tree.Kind.METHOD_INVOCATION);
    }

    @Override
    public void visitNode(Tree tree) {
        if (hasSemantic()) {
            if (tree.is(Tree.Kind.METHOD_INVOCATION)) {
                MethodInvocationTree methodInvocationTree = (MethodInvocationTree) tree;

                if (methodInvocationTree.symbol().usages().get(0).name().equals("defineClass")) {
                    String memberSelectExpression = ((MemberSelectExpressionTree) methodInvocationTree.methodSelect()).expression().symbolType().fullyQualifiedName();

                    if (memberSelectExpression.equals("sun.misc.Unsafe")) {
                        reportIssue(tree, "sun.misc.Unsafe.defineClass is deprecated in Java 9+.USE java.lang.invoke.MethodHandles.Lookup.defineClass in Java 9+");
                    }
                }
            }
        }
    }
}
