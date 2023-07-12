package com.bosspvp.core.skills.visualeffects.types;

import com.bosspvp.api.skills.visualeffects.VisualEffectsManager;
import com.bosspvp.api.skills.visualeffects.template.BaseEffect;
import com.bosspvp.api.utils.MathUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class FractalTree extends BaseEffect {
    public Particle particle;
    private List<Branch> tree;

    private boolean initialized;

    private Vector fractalVector = new Vector(0, 10, 0);
    private Vector rotationAngle = new Vector(0, 0, 30);
    Vector rotatStep;
    private int branchesMax = 5;
    private double fractalMultiply = 1;

    private int treesAmount = 1;
    private Vector treeRotationStep;
    private int branches = 0;

    private boolean animated = false;
    private int drawedBranches;

    /**
     * Displays a FractalTree
     *
     * @param effectsManager <i>Sets manager where effect runnable will be saved<i/>
     *                       <p><p/>
     * @apiNote parameters examples:
     * <ul>
     *     <li>Standard: <b>thickness:</b> 0.3; <b>amountOfBranches:</b> 7; <b>fractalVector:</b> 0;5;0; <b>rotationAngle:</b> 0;0;40</li>
     *     <li>3D Tree: <b>thickness:</b> 0.3; <b>amountOfBranches:</b> 7; <b>fractalVector:</b> 2;5;2; <b>rotationAngle:</b> 10;10;40</li>
     *     <li>Drawing Tree: <b>fractalUpdateTime:</b> 50</li>
     * </ul>
     */
    public FractalTree(VisualEffectsManager effectsManager) {
        super(effectsManager);
    }

    @Override
    public void onRun() {
        if (getOrigin() == null) {
            cancel(false);
            return;
        }
        Location location = getOrigin().updateLocation();

        if (!initialized) {
            tree = new ArrayList<>();
            Vector begin = new Vector(0, 0, 0);
            Vector end = fractalVector.clone();
            Branch root = new Branch(begin, end);
            tree.add(root);
            rotatStep = new Vector(treeRotationStep.getX() == -0.01 ?
                    360.0 / treesAmount : treeRotationStep.getX(),
                    treeRotationStep.getY() == -0.01 ?
                            360.0 / treesAmount : treeRotationStep.getY(),
                    treeRotationStep.getZ() == -0.01 ?
                            360.0 / treesAmount : treeRotationStep.getZ());

            for (int i = 1; i < branchesMax; i++) {
                setupBranch();
                branches++;
            }
            initialized = true;
        }

        if (animated) {
            if(drawedBranches>=tree.size()){
                cancel(true);
                return;
            }
            Vector rotateVector = new Vector();
            for (int i = 0; i < treesAmount; i++) {
                tree.get(0).show(location.clone(), rotateVector.add(rotatStep));
            }

        } else {
            for (Branch branch : tree) {
                Vector rotateVector = new Vector();
                for (int i = 0; i < treesAmount; i++) {
                    branch.show(location.clone(), rotateVector.add(rotatStep));
                }

            }
        }



    }

    @Override
    protected void onFinish() {

    }

    @Override
    protected void onClone(BaseEffect cloned) {

    }

    private void setupBranch() {
        List<Branch> list = new ArrayList<>();
        for (Branch current : tree) {
            //if the current Branch has no children: add them
            if (!current.finished) {
                list.add(current.branchA());
                list.add(current.branchB());
            }
            //now that Branch has children
            current.finished = true;
        }
        tree.addAll(list);
    }

    private class Branch {
        public Vector begin;
        public Vector end;
        private Vector stepSize;
        private Vector addToLocation;
        protected int particles;
        public boolean finished = false;

        protected int current_draw_step;

        private Branch childA;
        private Branch childB;

        Branch(Vector begin, Vector end) {
            this.begin = begin;
            this.end = end;

            Vector link = end.clone().subtract(begin);
            float length = (float) link.length();
            link.normalize();

            particles = (int) (length / thickness.getValue());

            float ratio = length / particles;

            stepSize = link.multiply(ratio);
        }

        void show(Location location, Vector rotation) {
            if (animated && particles < current_draw_step) {
                if(particles!=-1) {
                    particles = -1;
                    drawedBranches++;
                }
                if (childA != null) childA.show(location.clone(), rotation);
                if (childB != null) childB.show(location.clone(), rotation);
                return;
            }
            Vector v1 = stepSize.clone().add(begin);
            MathUtils.rotateAroundX(v1,rotation.getX() * MathUtils.degreesToRadians);
            MathUtils.rotateAroundY(v1,rotation.getY() * MathUtils.degreesToRadians);
            MathUtils.rotateAroundZ(v1,rotation.getZ() * MathUtils.degreesToRadians);
            Vector v2 = stepSize.clone().multiply(2).add(begin);
            MathUtils.rotateAroundX(v2,rotation.getX() * MathUtils.degreesToRadians);
            MathUtils.rotateAroundY(v2,rotation.getY() * MathUtils.degreesToRadians);
            MathUtils.rotateAroundZ(v2,rotation.getZ() * MathUtils.degreesToRadians);

            Vector step = v2.subtract(v1);
            addToLocation = v1.subtract(step);
            location.add(addToLocation);
            if (!animated) {
                for (int i = 0; i <= particles; i++) {
                    displayParticle(particle, location.add(step));
                }
            } else {
                displayParticle(particle, location.add(step.clone().multiply(current_draw_step)));
                current_draw_step++;
            }
        }

        Branch branchA() {
            Vector dir = end.clone().subtract(begin);
            MathUtils.rotateAroundX(dir,(Math.PI / 180) * rotationAngle.getX());
            MathUtils.rotateAroundY(dir,(Math.PI / 180) * rotationAngle.getY());
            MathUtils.rotateAroundZ(dir,(Math.PI / 180) * rotationAngle.getZ());
            dir.multiply(0.67 * fractalMultiply);
            Vector newEnd = end.clone().add(dir);
            childA = new Branch(end, newEnd);
            return childA;
        }

        Branch branchB() {
            Vector dir = end.clone().subtract(begin);
            MathUtils.rotateAroundX(dir,(Math.PI / 180) * -rotationAngle.getX());
            MathUtils.rotateAroundY(dir,(Math.PI / 180) * -rotationAngle.getY());
            MathUtils.rotateAroundZ(dir,(Math.PI / 180) * -rotationAngle.getZ());
            dir.multiply(0.67 * fractalMultiply);
            Vector newEnd = end.clone().add(dir);
            childB = new Branch(end, newEnd);
            return childB;
        }


    }
}
