/**
 * An interface for implementating a judging engine.
 *
 * @author: Tanzid Sultan
 * ID# 1430660, Email: tanzids@student.unimelb.edu.au
 */
public interface JudgingEngine {

    /**
    * Method for acquiring scenarios (either from file or randomly generated)
    */
    public void loadScenarios();

    /**
    * Method for judging given scenarios
    */
    public void judgeScenarios();

    /**
    * Decision maker for which group to save within a given scenario
    * @param sc the scenario
    * @return decision which group to save
    */
    public int decide(Scenario sc); 

    /**
    * Method for generating statistics for judged scenarios
    */
    public void generateStatistics();

}