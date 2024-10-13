export class ConditionalExecutor {
  static executeFirst(conditions: Array<[boolean, () => void]>): boolean {
    return conditions.some(([shouldExecute, action]) => shouldExecute && (action(), true));
  }

  static async executeFirstWithResult<T>(
    conditions: Array<[boolean, () => T | Promise<T>]>,
  ): Promise<T | undefined> {
    for (const [shouldExecute, action] of conditions) {
      if (shouldExecute) {
        return await action();
      }
    }
    return undefined;
  }

  static executeAll(conditions: Array<[boolean, () => void]>): number {
    return conditions.filter(([shouldExecute, action]) => shouldExecute && (action(), true)).length;
  }

  static async executeAllWithResults<T>(
    conditions: Array<[boolean, () => T | Promise<T>]>,
  ): Promise<T[]> {
    const results: T[] = [];
    for (const [shouldExecute, action] of conditions) {
      if (shouldExecute) {
        results.push(await action());
      }
    }
    return results;
  }
}
