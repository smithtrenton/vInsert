package org.vinsert.core.control;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.log4j.Logger;
import org.vinsert.api.APIModule;
import org.vinsert.api.MethodContext;
import org.vinsert.api.event.EventHandler;
import org.vinsert.api.random.LoginRequired;
import org.vinsert.api.random.RandomSolver;
import org.vinsert.api.random.impl.*;
import org.vinsert.core.Session;
import org.vinsert.core.event.PulseEvent;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A management class for random events.
 */
public final class RandomManager {
    private static final Logger logger = Logger.getLogger(RandomManager.class);
    private final CopyOnWriteArrayList<RandomSolver> randomSolvers;
    private final Injector injector;
    private final Session session;

    public RandomManager(Session session) {
        APIModule module = new APIModule();
        module.setContext(new MethodContext(session));
        this.injector = Guice.createInjector(module);
        this.session = session;
        randomSolvers = new CopyOnWriteArrayList<>();
        registerSolver(new LoginRandom());
        registerSolver(new ExamRandom());
        registerSolver(new SandwichLadyRandom());
        registerSolver(new WelcomeScreenRandom());
        registerSolver(new StrangeBoxRandom());
        registerSolver(new RunAwayRandom());
        registerSolver(new MimeRandom());
        registerSolver(new TalkToRandom());
        registerSolver(new PrisonPeteRandom());
        registerSolver(new DrillDemonRandom());
        registerSolver(new ScapeIslandRandom());
        registerSolver(new QuizRandom());
        registerSolver(new MollyRandom());
        registerSolver(new MazeRandom());
        registerSolver(new FreakyForesterRandom());
        registerSolver(new PinballRandom());
        session.getEnvironment().getEventBus().register(this, false);
        logger.info("Initialized with " + randomSolvers.size() + " solvers.");
    }

    public void registerSolver(RandomSolver solver) {
        injector.injectMembers(solver);
        randomSolvers.add(solver);
        logger.info("Registered solver " + solver.getClass().getSimpleName());
    }

    public void deregisterSolver(RandomSolver solver) {
        randomSolvers.remove(solver);
        logger.info("De-registered solver " + solver.getClass().getSimpleName());
    }

    /**
     * Checks all randoms to see if any are activated.
     * Activates them when required.
     */
    @EventHandler
    public void pulse(PulseEvent event) {
        if (session.getState() != Session.State.ACTIVE) {
            return;
        }

        for (RandomSolver solver : randomSolvers) {
            solver.client = session.getClient();
            solver.session = session;
            if (stateRequirementMet(solver) && solver.canRun()) {
                solver.session.getEnvironment().getEventBus().register(solver, true);
                session.getScriptManager().setInRandom(true);
                logger.info("Executing solver " + solver.getClass().getSimpleName());
                solver.execute();
                logger.info("Executed solver " + solver.getClass().getSimpleName());
            }
            session.getScriptManager().setInRandom(false);
            solver.session.getEnvironment().getEventBus().deregister(solver);
        }
    }

    private boolean stateRequirementMet(RandomSolver solver) {
        return (solver.getClass().getAnnotation(LoginRequired.class) == null || solver.isLoggedIn());
    }
}
