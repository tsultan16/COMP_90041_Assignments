
public interface JudgingEngine {

    // method for acquiring scenarios (either from file or randomly generated)
    public void loadScenarios();

    // method for judging given scenarios
    public void judgeScenarios();

    /**
    * Decision maker for which group to save within a given scenario
    * @param Scenario sc: the ethical dilemma
    * @return decision: which group to save
    */
    public int decide(Scenario sc); 

    // method for generating statistics for judged scenarios
    public void generateStatistics();

}